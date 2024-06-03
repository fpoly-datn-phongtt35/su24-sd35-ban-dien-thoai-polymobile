### Dự án bán điện thoại IPhone Poly Mobile
# su24-sd35-ban-dien-thoai-polymobile


## Yêu cầu xây dựng theo cấu trúc thư mục như sau

### Cấu trúc src
>**└── polystore**
>&emsp;&emsp;&emsp;├── **config**
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── constant
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── exeption
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── jwt
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── listener
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── BeanConfig.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;└── SecurityConfig.java
>&emsp;&emsp;&emsp;├── **core**
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── ***admin***
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── package_controller_example
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── controller
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── converter
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── dto
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── model
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── request
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── response
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── repository
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── impl
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── service
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── impl
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── don_hang
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── dot_giam_gia
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── giao_dich
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── ma_giam_gia
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── nhan_vien
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── san_pham
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── san_pham_chi_tiet
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── tai_khoan
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── thong_ke
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── ***client***
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── controller
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── example_class.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── models
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── request
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── example_class.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── response
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── example_class.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── repositories
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── servies
>&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── impl
>&emsp;&emsp;&emsp;│&emsp;&emsp;└── **common**
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── login
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── controller
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── model
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── repository
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;└── service
>&emsp;&emsp;&emsp;├── **entity**
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── BinhLuan.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── BinhLuanChiTiet.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── Camera.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── CongNgheSac.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── Cpu.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── DanhGia.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;└── ***etc*..**.java
>&emsp;&emsp;&emsp;├── **repository**
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── BaiVietRepository.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── BinhLuanChiTietRepository.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── BinhLuanRepository.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── CameraRepository.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── CongNgheSacRepository.java
>&emsp;&emsp;&emsp;│&emsp;&emsp;└── ***etc*..**.java
>&emsp;&emsp;&emsp;└── **utils**
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;└── Utils.java
>
### Cấu trúc resources
>**└──resources**
>&emsp;&emsp;&emsp;├── **static**
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── js
>&emsp;&emsp;&emsp;│&emsp;&emsp;├── css
>&emsp;&emsp;&emsp;│&emsp;&emsp;└── vendor
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── bootstrap
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── chart.js
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── datatables
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── fontawesome-free
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── jquery
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── jquery-easing&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
>&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── ...
>&emsp;&emsp;&emsp;└── **templates**
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── ***admin***
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;├── layout-dashboard
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── layout.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── util.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;├── promotion
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── create.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── edit.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── list.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;├── san-pham-chi-tiet
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── mau-sac.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── pin.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;└── ...
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── create.html&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── list.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── ....html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── ***client***
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;├── layout-dashboard
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── layout.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── util.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;├── promotion
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── create.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── edit.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── list.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;├── san-pham-chi-tiet
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;├── mau-sac.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;│&emsp;&emsp;└── pin.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;└── ...
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── create.html&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;├── list.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;│&emsp;&emsp;&emsp;&emsp;&emsp;└── ....html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;└── ***utils***
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;├── authentication.html
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;└── ....html
>
>
>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;