package com.poly.polystore.core.admin.ban_hang.model.response;

import com.poly.polystore.entity.PhieuGiamGia;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.SanPhamChiTiet}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SanPhamDetailBanHang implements Serializable {
    @Id
    private Integer id;
    private String rom;
    private String mauSacId;
    @Transient
    private PhieuGiamGia dotGiamGia;
    private String soLuong;
}