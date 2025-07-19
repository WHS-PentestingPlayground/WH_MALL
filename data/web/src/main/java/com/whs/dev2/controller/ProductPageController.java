package com.whs.dev2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
public class ProductPageController {
    
    @GetMapping("/products")
    public String productsPage() {
        return "products";
    }
    
    @GetMapping("/products/{productId}")
    public String productDetailPage(@PathVariable String productId, Model model) {
        Map<String, Object> product = getProductData(productId);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "product-detail";
    }
    
    private Map<String, Object> getProductData(String productId) {
        Map<String, Object> product = new HashMap<>();
        
        switch (productId) {
            case "smartphone-pro":
                product.put("name", "스마트폰 Pro");
                product.put("icon", "📱");
                product.put("price", "1,200,000");
                product.put("description", "최신 기술이 적용된 프리미엄 스마트폰입니다. 고성능 카메라와 긴 배터리 수명으로 일상생활을 더욱 편리하게 만들어드립니다.");
                product.put("specs", Arrays.asList("6.7인치 OLED 디스플레이", "48MP 트리플 카메라", "5000mAh 대용량 배터리", "5G 네트워크 지원"));
                product.put("features", Arrays.asList("고성능 프로세서", "고해상도 카메라", "빠른 충전", "무선 충전"));
                product.put("detailDescription", "스마트폰 Pro는 최신 기술을 모두 담은 프리미엄 스마트폰입니다. 6.7인치 OLED 디스플레이는 생생한 색감과 선명한 화질을 제공하며, 48MP 트리플 카메라로 전문가급 사진 촬영이 가능합니다. 5000mAh 대용량 배터리와 5G 네트워크 지원으로 하루 종일 빠른 속도로 사용할 수 있습니다.");
                
                Map<String, String> detailedSpecs = new LinkedHashMap<>();
                detailedSpecs.put("디스플레이", "6.7인치 OLED (2778 x 1284)");
                detailedSpecs.put("프로세서", "A17 Pro 칩");
                detailedSpecs.put("메모리", "8GB RAM + 256GB/512GB/1TB");
                detailedSpecs.put("카메라", "48MP 메인 + 12MP 초광각 + 12MP 망원");
                detailedSpecs.put("배터리", "5000mAh");
                detailedSpecs.put("충전", "25W 유선, 15W 무선");
                detailedSpecs.put("운영체제", "iOS 17");
                product.put("detailedSpecs", detailedSpecs);
                break;
                
            case "laptop-ultra":
                product.put("name", "노트북 Ultra");
                product.put("icon", "💻");
                product.put("price", "2,500,000");
                product.put("description", "업무와 엔터테인먼트를 위한 고성능 노트북입니다. 빠른 처리 속도와 선명한 화질로 모든 작업을 효율적으로 처리할 수 있습니다.");
                product.put("specs", Arrays.asList("15.6인치 4K 디스플레이", "Intel i7 프로세서", "16GB RAM + 512GB SSD", "NVIDIA RTX 그래픽"));
                product.put("features", Arrays.asList("고성능 프로세서", "4K 디스플레이", "고속 SSD", "게이밍 그래픽"));
                product.put("detailDescription", "노트북 Ultra는 업무와 엔터테인먼트를 모두 만족시키는 고성능 노트북입니다. 15.6인치 4K 디스플레이는 놀라운 화질을 제공하며, Intel i7 프로세서와 16GB RAM으로 빠른 작업 처리가 가능합니다. NVIDIA RTX 그래픽카드로 게임과 영상 편집도 원활하게 할 수 있습니다.");
                
                detailedSpecs = new LinkedHashMap<>();
                detailedSpecs.put("디스플레이", "15.6인치 4K (3840 x 2160)");
                detailedSpecs.put("프로세서", "Intel Core i7-12700H");
                detailedSpecs.put("메모리", "16GB DDR4 RAM");
                detailedSpecs.put("저장장치", "512GB NVMe SSD");
                detailedSpecs.put("그래픽", "NVIDIA RTX 3060 6GB");
                detailedSpecs.put("배터리", "86Wh");
                detailedSpecs.put("운영체제", "Windows 11 Pro");
                product.put("detailedSpecs", detailedSpecs);
                break;
                
            case "wireless-earbuds":
                product.put("name", "무선 이어폰");
                product.put("icon", "🎧");
                product.put("price", "350,000");
                product.put("description", "프리미엄 사운드 품질을 제공하는 무선 이어폰입니다. 노이즈 캔슬링 기능과 편안한 착용감으로 완벽한 음악 감상을 경험하세요.");
                product.put("specs", Arrays.asList("액티브 노이즈 캔슬링", "24시간 재생 시간", "방수 IPX4 등급", "터치 컨트롤"));
                product.put("features", Arrays.asList("고음질 사운드", "노이즈 캔슬링", "긴 배터리 수명", "방수 기능"));
                product.put("detailDescription", "무선 이어폰은 프리미엄 사운드 품질을 제공하는 최고급 무선 이어폰입니다. 액티브 노이즈 캔슬링 기능으로 외부 소음을 차단하고, 24시간의 긴 재생 시간으로 하루 종일 음악을 즐길 수 있습니다. IPX4 방수 등급으로 운동 중에도 안전하게 사용할 수 있습니다.");
                
                detailedSpecs = new LinkedHashMap<>();
                detailedSpecs.put("드라이버", "10mm 다이나믹 드라이버");
                detailedSpecs.put("주파수 응답", "20Hz - 20kHz");
                detailedSpecs.put("노이즈 캔슬링", "액티브 노이즈 캔슬링");
                detailedSpecs.put("배터리", "6시간 (이어폰) + 18시간 (케이스)");
                detailedSpecs.put("충전", "USB-C, 무선 충전");
                detailedSpecs.put("방수", "IPX4 등급");
                detailedSpecs.put("연결", "Bluetooth 5.2");
                product.put("detailedSpecs", detailedSpecs);
                break;
                
            case "smartwatch":
                product.put("name", "스마트워치");
                product.put("icon", "⌚");
                product.put("price", "450,000");
                product.put("description", "건강 관리와 일상 편의 기능을 모두 갖춘 스마트워치입니다. 심박수 모니터링과 GPS 기능으로 건강한 라이프스타일을 지원합니다.");
                product.put("specs", Arrays.asList("1.4인치 AMOLED 터치스크린", "심박수 모니터링", "GPS 내비게이션", "7일 배터리 수명"));
                product.put("features", Arrays.asList("건강 모니터링", "GPS 내비게이션", "알림 기능", "운동 추적"));
                product.put("detailDescription", "스마트워치는 건강 관리와 일상 편의 기능을 모두 갖춘 스마트워치입니다. 1.4인치 AMOLED 터치스크린으로 선명한 화질을 제공하며, 심박수 모니터링과 GPS 내비게이션 기능으로 건강한 라이프스타일을 지원합니다. 7일의 긴 배터리 수명으로 자주 충전할 필요가 없습니다.");
                
                detailedSpecs = new LinkedHashMap<>();
                detailedSpecs.put("디스플레이", "1.4인치 AMOLED (454 x 454)");
                detailedSpecs.put("센서", "심박수, 가속도계, 자이로스코프");
                detailedSpecs.put("GPS", "내장 GPS + GLONASS");
                detailedSpecs.put("배터리", "300mAh (7일 사용)");
                detailedSpecs.put("방수", "5ATM (수영 가능)");
                detailedSpecs.put("연결", "Bluetooth 5.0");
                detailedSpecs.put("운영체제", "Wear OS");
                product.put("detailedSpecs", detailedSpecs);
                break;
                
            case "mirrorless-camera":
                product.put("name", "미러리스 카메라");
                product.put("icon", "📷");
                product.put("price", "3,200,000");
                product.put("description", "전문가급 사진과 영상을 촬영할 수 있는 미러리스 카메라입니다. 고해상도 센서와 다양한 렌즈 옵션으로 창의적인 촬영이 가능합니다.");
                product.put("specs", Arrays.asList("24.1MP 풀프레임 센서", "4K 영상 촬영", "5축 손떨림 보정", "Wi-Fi 연결"));
                product.put("features", Arrays.asList("고해상도 센서", "4K 영상 촬영", "손떨림 보정", "무선 연결"));
                product.put("detailDescription", "미러리스 카메라는 전문가급 사진과 영상을 촬영할 수 있는 최고급 카메라입니다. 24.1MP 풀프레임 센서로 놀라운 화질을 제공하며, 4K 영상 촬영과 5축 손떨림 보정으로 안정적인 촬영이 가능합니다. Wi-Fi 연결로 스마트폰과 연동하여 즉시 공유할 수 있습니다.");
                
                detailedSpecs = new LinkedHashMap<>();
                detailedSpecs.put("센서", "24.1MP 풀프레임 CMOS");
                detailedSpecs.put("ISO", "100-51200 (확장 50-204800)");
                detailedSpecs.put("연속 촬영", "10fps");
                detailedSpecs.put("영상", "4K 30fps, Full HD 120fps");
                detailedSpecs.put("손떨림 보정", "5축 IBIS");
                detailedSpecs.put("연결", "Wi-Fi, Bluetooth");
                detailedSpecs.put("배터리", "NP-FZ100 (약 360장)");
                product.put("detailedSpecs", detailedSpecs);
                break;
                
            case "gaming-console":
                product.put("name", "게이밍 콘솔");
                product.put("icon", "🎮");
                product.put("price", "550,000");
                product.put("description", "최신 게임을 최고의 품질로 즐길 수 있는 게이밍 콘솔입니다. 빠른 로딩 속도와 부드러운 그래픽으로 몰입감 있는 게임 경험을 제공합니다.");
                product.put("specs", Arrays.asList("4K 게이밍 지원", "1TB SSD 저장공간", "Ray Tracing 기술", "듀얼센스 컨트롤러"));
                product.put("features", Arrays.asList("4K 게이밍", "고속 SSD", "Ray Tracing", "무선 컨트롤러"));
                product.put("detailDescription", "게이밍 콘솔은 최신 게임을 최고의 품질로 즐길 수 있는 차세대 게임 콘솔입니다. 4K 게이밍 지원과 Ray Tracing 기술로 놀라운 그래픽을 제공하며, 고속 SSD로 빠른 로딩 속도를 자랑합니다. 듀얼센스 컨트롤러로 몰입감 있는 게임 경험을 할 수 있습니다.");
                
                detailedSpecs = new LinkedHashMap<>();
                detailedSpecs.put("프로세서", "AMD Zen 2 8코어");
                detailedSpecs.put("그래픽", "AMD RDNA 2 (10.3 TFLOPS)");
                detailedSpecs.put("메모리", "16GB GDDR6");
                detailedSpecs.put("저장장치", "1TB NVMe SSD");
                detailedSpecs.put("해상도", "4K (최대 120fps)");
                detailedSpecs.put("Ray Tracing", "하드웨어 가속");
                detailedSpecs.put("컨트롤러", "듀얼센스 무선 컨트롤러");
                product.put("detailedSpecs", detailedSpecs);
                break;
                
            default:
                return null;
        }
        
        return product;
    }
} 