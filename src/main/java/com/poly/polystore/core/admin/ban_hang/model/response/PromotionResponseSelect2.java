package com.poly.polystore.core.admin.ban_hang.model.response;

import com.poly.polystore.core.admin.san_pham.model.reponse.DataSourcesSelect2;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PromotionResponseSelect2 implements Serializable {
    private List<PromotionResponse> results;
    private Pagination pagination;
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Pagination {
        Boolean more;
    }
    /**
     * DTO for {@link com.poly.polystore.entity.MaGiamGia}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class PromotionResponse implements Serializable {
        Integer id;
        String text;
    }

}

