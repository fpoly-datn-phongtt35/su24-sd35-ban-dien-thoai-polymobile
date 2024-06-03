### Dự án bán điện thoại IPhone Poly Mobile
# su24-sd35-ban-dien-thoai-polymobile


## Yêu cầu xây dựng theo cấu trúc thư mục như sau

### Cấu trúc src

    └── polystore
    ├── config
    │   ├── constant
    │   ├── exeption
    │   ├── jwt
    │   ├── listener
    │   ├── BeanConfig.java
    │   └── SecurityConfig.java
    ├── core
    │   ├── admin
    │   │   ├── package_controller_example
    │   │   │   ├── controller
    │   │   │   ├── converter
    │   │   │   ├── dto
    │   │   │   ├── model
    │   │   │   │   ├── request
    │   │   │   │   └── response
    │   │   │   ├── repository
    │   │   │   │   └── impl
    │   │   │   └── service
    │   │   │       └── impl
    │   │   ├── don_hang
    │   │   ├── dot_giam_gia
    │   │   ├── giao_dich
    │   │   ├── ma_giam_gia
    │   │   ├── nhan_vien
    │   │   ├── san_pham
    │   │   ├── san_pham_chi_tiet
    │   │   ├── tai_khoan
    │   │   └── thong_ke
    │   ├── client
    │   │   ├── controller
    │   │   │   └── example_class.java
    │   │   ├── models
    │   │   │   ├── request
    │   │   │   │   └── example_class.java
    │   │   │   └── response
    │   │   │       └── example_class.java
    │   │   ├── repositories
    │   │   └── servies
    │   │       └── impl
    │   └── common
    │       └── login
    │           ├── controller
    │           │   └── AuthenticationController.java
    │           ├── model
    │           │   ├── request
    │           │   └── response
    │           ├── repository
    │           └── service
    │               ├── AuthenticationService.java
    │               ├── impl
    │               ├── JwtService.java
    │               └── TaiKhoanService.java
    ├── entity
    │   ├── BinhLuan.java
    │   ├── BinhLuanChiTiet.java
    │   ├── Camera.java
    │   ├── CongNgheSac.java
    │   ├── Cpu.java
    │   ├── DanhGia.java
    │   └── etc...java
    ├── repository
    │   ├── BaiVietRepository.java
    │   ├── BinhLuanChiTietRepository.java
    │   ├── BinhLuanRepository.java
    │   ├── CameraRepository.java
    │   ├── CongNgheSacRepository.java
    │   └── etc...java
    └── utils
        └── Utils.java

### Cấu trúc resources

    └──resources
    ├── static
    │   └── vendor
    │       ├── bootstrap
    │       ├── chart.js
    │       ├── datatables
    │       ├── fontawesome-free
    │       ├── jquery
    │       ├── jquery-easing        
    │       └── ...
    └── templates
        ├── admin
        │   ├── layout-dashboard
        │   │   ├── layout.html
        │   │   └── util.html
        │   ├── promotion
        │   │   ├── create.html
        │   │   ├── edit.html
        │   │   └── list.html
        │   ├── san-pham-chi-tiet
        │   │   ├── mau-sac.html
        │   │   └── pin.html
        │   └── ...
        │       ├── create.html            
        │       ├── list.html
        │       └── ....html
        ├── client
        │   ├── layout-dashboard
        │   │   ├── layout.html
        │   │   └── util.html
        │   ├── promotion
        │   │   ├── create.html
        │   │   ├── edit.html
        │   │   └── list.html
        │   ├── san-pham-chi-tiet
        │   │   ├── mau-sac.html
        │   │   └── pin.html
        │   └── ...
        │       ├── create.html            
        │       ├── list.html
        │       └── ....html
        └── utils
            ├── authentication.html
            └── ....html


        
