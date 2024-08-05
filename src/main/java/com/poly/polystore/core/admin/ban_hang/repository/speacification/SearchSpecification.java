package com.poly.polystore.core.admin.ban_hang.repository.speacification;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SearchSpecification<T> {

//    public Specification<T> getPredicate(final String key, final Operation operation, final Object value) {
////        return new Specification<T>() {
////            @Override
////            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//////                switch (operation) {
//////                    case EQUALITY -> {
//////                        return criteriaBuilder.equal(root.get(key),value);
//////                    }
//////                    case GREATER_THAN ->{
//////                        return criteriaBuilder.greaterThan(root.get(key),value.toString());
//////                    }
//////                    case LESS_THAN ->{
//////                        return criteriaBuilder.lessThan(root.get(key),value.toString());
//////                    }
//////                    case '~' ->{
//////                        return criteriaBuilder.like(root.get(key),"%"+value+"%");
//////                    }
//////
//////                }
////            }
////        };
//    }

}
