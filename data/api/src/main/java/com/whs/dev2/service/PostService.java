package com.whs.dev2.service;

import com.whs.dev2.dto.PostRequestDto;
import com.whs.dev2.dto.PostResponseDto;
import com.whs.dev2.entity.Post;
import com.whs.dev2.entity.User;
import com.whs.dev2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.whs.dev2.util.AesEncryptor;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPostById(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto dto, User user, MultipartFile file, String tokenRole) {
        // JWT 토큰의 role로 admin 권한 체크
        if (!"partner".equals(tokenRole)) {
            throw new IllegalArgumentException("공지사항 작성 권한이 없습니다. partner 권한이 필요합니다.");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            validateFileName(fileName);

            String uploadDir = "/tmp/uploads/";
            String uploadPath = uploadDir + fileName;

            try {
                // 디렉토리 보장
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 1. 원본 파일 저장
                File destination = new File(uploadPath);
                file.transferTo(destination);
                post.setFileName(fileName);

                // 2. scp_transfer.sh 실행하여 원본 파일을 web-server로 전송
                ProcessBuilder pb = new ProcessBuilder(
                        "/app/scripts/scp_transfer.sh",
                        uploadPath
                );

                // 상세 로깅을 위해 pb.inheritIO() 대신 직접 스트림 처리
                Process process = pb.start();

                // SCP 명령어의 표준 출력 로깅
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("SCP stdout: " + line);
                    }
                }

                // SCP 명령어의 표준 에러 로깅
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("SCP stderr: " + line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("scp 전송 실패. Exit code: " + exitCode);
                }

                // 2.1. web-server에서 scp로 받은 파일을 최종 목적지로 이동
                String remoteMoveCommand = String.format(
                        "mv -v /home/ctfuser/%s /usr/local/tomcat/webapps/ROOT/uploads/. && ls -la /usr/local/tomcat/webapps/ROOT/uploads/",
                        fileName
                );
                ProcessBuilder sshMvPb = new ProcessBuilder(
                        "ssh", "-i", "/app/scripts/id_rsa",
                        "-o", "StrictHostKeyChecking=no",
                        "-o", "UserKnownHostsFile=/dev/null",
                        "ctfuser@net_robotics_web",
                        remoteMoveCommand
                );
                // 상세 로깅을 위해 pb.inheritIO() 대신 직접 스트림 처리
                Process sshMvProcess = sshMvPb.start();

                // mv 명령어의 표준 출력 로깅
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(sshMvProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("SSH mv stdout: " + line);
                    }
                }

                // mv 명령어의 표준 에러 로깅
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(sshMvProcess.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("SSH mv stderr: " + line);
                    }
                }

                int mvExitCode = sshMvProcess.waitFor();
                if (mvExitCode != 0) {
                    throw new RuntimeException("원격지 파일 이동(mv) 실패. Exit code: " + mvExitCode);
                }

                // 3. 파일 암호화 및 DB 저장을 위한 데이터 생성
                String encryptedFileName;
                if (fileName.contains(".")) {
                    encryptedFileName = fileName.replaceFirst("\\.[^.]+$", ".enc");
                } else {
                    encryptedFileName = fileName + ".enc";
                }
                String encryptedPath = uploadDir + encryptedFileName;
                String encryptedBase64 = AesEncryptor.encryptFileToBase64(uploadPath, encryptedPath);

                post.setEncryptedFileName(encryptedFileName);
                post.setEncryptedFileData(encryptedBase64);

                System.out.println("uploadPath: " + uploadPath);
                System.out.println("encryptedPath: " + encryptedPath);
                // 4. api-server에 임시 저장된 원본 및 암호화 파일 삭제
                new File(uploadPath).delete();
                new File(encryptedPath).delete();

            } catch (Exception e) {
                throw new RuntimeException("파일 처리 및 암호화 중 오류", e);
            }
        }

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }


    @Transactional
    public void updatePost(Long id, PostRequestDto dto, User user, MultipartFile file) {
        Post post = findPostById(id);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());


        // 파일 교체 처리
        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제
            if (post.getFileName() != null) {
                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
                File oldFile = new File(uploadDir, post.getFileName());
                if (oldFile.exists()) oldFile.delete();
            }

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String newFileName = file.getOriginalFilename();
            validateFileName(newFileName);
            File destination = new File(dir, newFileName);
            try {
                file.transferTo(destination);
                post.setFileName(newFileName);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = findPostById(id);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }

        // 파일 삭제
        if (post.getFileName() != null) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File file = new File(uploadDir, post.getFileName());
            if (file.exists()) file.delete();
        }

        postRepository.delete(post);
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private void validateFileName(String fileName) {
        if (fileName == null || !fileName.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException("허용되지 않은 파일명입니다.");
        }
    }
}
