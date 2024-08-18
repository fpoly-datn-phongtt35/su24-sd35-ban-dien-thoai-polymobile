package com.poly.polystore.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Select2Response implements Serializable {
    private List<Result> results;
    private Pagination pagination;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagination {
        Boolean more;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        Integer id;
        String text;

        public Result(Integer id, String text1, String... texts) {
            this.id = id;
            this.text = Arrays.stream(texts).reduce("", (a, b) -> a + " " + b);
        }
    }
}
