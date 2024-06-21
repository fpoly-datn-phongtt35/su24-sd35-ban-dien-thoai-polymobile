package com.poly.polystore.core.admin.san_pham.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AllSanPhamResponse implements Serializable {
    private Integer id;
    private String ten;
    private String gpu;
    private String link;
    private Boolean deleted;
}