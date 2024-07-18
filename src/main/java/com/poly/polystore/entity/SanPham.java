package com.poly.polystore.entity;

import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamDataTable;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.List;
import java.util.Set;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SAN_PHAM")

@SqlResultSetMapping(
        name = "SanPhamDataTableMapping",
        classes = @ConstructorResult(
                targetClass = SanPhamDataTable.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "anhSanPham", type = String.class),
                        @ColumnResult(name = "tenSanPham", type = String.class),
                        @ColumnResult(name = "series", type = String.class),
                        @ColumnResult(name = "danhSachMauSac", type = String.class),
                        @ColumnResult(name = "danhSachRom", type = String.class),
                        @ColumnResult(name = "soLuong", type = Integer.class),
                        @ColumnResult(name = "thoiGianBaoHanh", type = String.class),
                        @ColumnResult(name = "trangThai", type = String.class)
                }
        )

)

@NamedNativeQuery(
        name = "findAllSanPhamDataTable",
        query = """
               
                WITH DistinctColors AS (
                                       SELECT sp.ID AS San_Pham_ID, ms.MA AS MA_MAU, ms.TEN AS TEN_MAU
                                       FROM dbo.SAN_PHAM sp
                                       LEFT JOIN dbo.SAN_PHAM_CHI_TIET spct ON sp.ID = spct.SAN_PHAM_ID
                                       LEFT JOIN dbo.MAU_SAC ms ON spct.MAU_SAC_ID = ms.ID
                                       WHERE ms.TEN IS NOT NULL
                                       GROUP BY sp.ID, ms.MA, ms.TEN
                                   ),
                                   DistinctROMs AS (
                                       SELECT sp.ID AS San_Pham_ID, spct.ROM
                                       FROM dbo.SAN_PHAM sp
                                       JOIN dbo.SAN_PHAM_CHI_TIET spct ON sp.ID = spct.SAN_PHAM_ID
                                       WHERE spct.ROM IS NOT NULL
                                       GROUP BY sp.ID, spct.ROM
                                   ),
                                   ColorAggregates AS (
                                       SELECT San_Pham_ID, STRING_AGG(CONCAT_WS(':',MA_MAU,TEN_MAU), ',') AS Danh_Sach_Mau_Sac
                                       FROM DistinctColors
                                       GROUP BY San_Pham_ID
                                   ),
                                   ROMAggregates AS (
                                       SELECT San_Pham_ID, STRING_AGG(ROM, ',') AS Danh_Sach_ROM
                                       FROM DistinctROMs
                                       GROUP BY San_Pham_ID
                                   )
                                   SELECT
                                       sp.ID AS id,
                                       sp.TEN_SAN_PHAM AS tenSanPham,
                                       sr.TEN as series,
                                       anh.URL as anhSanPham,
                                       ca.Danh_Sach_Mau_Sac as danhSachMauSac,
                                       ra.Danh_Sach_ROM as danhSachRom,
                                       COUNT(i.IMEI) AS soLuong,
                                       sp.THOI_GIAN_BAO_HANH as thoiGianBaoHanh,
                                       sp.TRANG_THAI as trangThai
                                   FROM
                                       dbo.SAN_PHAM sp
                                   LEFT JOIN
                                           ANH anh ON sp.ANH_ID = anh.ID
                                   LEFT JOIN
                                           SERIES sr ON sp.SERIES_ID = sr.ID
                                   LEFT JOIN
                                       dbo.SAN_PHAM_CHI_TIET spct ON sp.ID = spct.SAN_PHAM_ID
                                   LEFT JOIN
                                       ColorAggregates ca ON sp.ID = ca.San_Pham_ID
                                   LEFT JOIN
                                       ROMAggregates ra ON sp.ID = ra.San_Pham_ID
                                   LEFT JOIN
                                       dbo.IMEI i ON spct.ID = i.SAN_PHAM_CHI_TIET_ID AND i.TRANG_THAI NOT LIKE N'DA_BAN'
                                   GROUP BY
                                       sp.ID, sp.TEN_SAN_PHAM, ca.Danh_Sach_Mau_Sac, ra.Danh_Sach_ROM, THOI_GIAN_BAO_HANH, sp.TRANG_THAI, sr.TEN,anh.URL
                """,
        resultSetMapping = "SanPhamDataTableMapping"
)


public class SanPham {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANH_ID")
    private Anh anh;

    @Nationalized
    @Column(name = "Ten_san_pham")
    private String tenSanPham;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BINH_LUAN_ID")
    private BinhLuan binhLuan;

    @Embedded
    private ManHinh manHinh;
    @Embedded
    private FontCamera cameraTruoc;
    @Embedded
    private BackCamera cameraSau;

    @Embedded
    private HeDieuHanhVaCpu heDieuHanhVaCpu;

    @Embedded
    private KetNoi ketNoi;

    @Embedded
    private PinVaSac pinVaSac;
    @Embedded
    private ThongTinChung thongTinChung;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "SAN_PHAM_KHUYEN_MAI",
            joinColumns = @JoinColumn(name = "SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "KHUYEN_MAI_ID")

    )
    private List<KhuyenMai> khuyenMai;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERIES_ID")
    private Series series;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "sanPham",
            cascade =  CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<SanPhamChiTiet> sanPhamChiTiet;

    @Column(name = "RAM")
    private String ram;

    @Nationalized
    @Column(name = "THOI_GIAN_BAO_HANH")
    private String thoiGianBaoHanh;

    @Enumerated(EnumType.STRING)
    @Column(name="TRANG_THAI")
    private SanPhamRepository.TrangThai trangThai;

    @Nationalized
    @Column(name = "MO_TA")
    private String moTa;

    @Nationalized
    @Column(name = "STT")
    private Integer stt;



}