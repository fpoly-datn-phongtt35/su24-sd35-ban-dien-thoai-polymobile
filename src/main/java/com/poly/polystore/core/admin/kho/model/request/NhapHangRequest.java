package com.poly.polystore.core.admin.kho.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NhapHangRequest implements Serializable {
    @NotNull
    private List<NhapHangImeis> data;
    private String note;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class NhapHangImeis implements Serializable {
        @Positive
        private Integer id;
        @NotNull
        private List<String> imeis;
    }
}
