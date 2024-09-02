package com.poly.polystore.core.client.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchForm {
    private String searchType;
    private String searchValue;
}