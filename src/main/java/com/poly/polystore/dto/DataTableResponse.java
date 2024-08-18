package com.poly.polystore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataTableResponse  implements Serializable {
    private Integer draw,recordsTotal,recordsFiltered;
    private List<?> data;
}
