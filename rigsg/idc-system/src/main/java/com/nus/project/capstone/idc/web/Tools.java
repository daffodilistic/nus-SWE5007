package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import org.springframework.http.ResponseEntity;

public class Tools {

   public static ResponseEntity<GeneralMessageEntity> genericFailureMessage(){
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data("Update failed. Request contains other illegal params. Pls check.").build());
    }
}
