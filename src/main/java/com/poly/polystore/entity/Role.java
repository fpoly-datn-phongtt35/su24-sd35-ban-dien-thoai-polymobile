package com.poly.polystore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Nationalized;


public enum Role {
    ROLE_ADMIN,
    ROLE_EMPLOYEE,
    ROLE_CUSTOMER,
    ROLE_GUEST

}