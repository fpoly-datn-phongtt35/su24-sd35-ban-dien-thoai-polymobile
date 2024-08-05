package com.poly.polystore.core.admin.ban_hang.model.response;

import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SanPhamDataTableBanHang implements Serializable {
    @Id
    private Integer id;
    private String anhUrl;
    private String tenSanPham;
    private SanPhamRepository.TrangThai trangThai;
    private Integer soLuong;

}