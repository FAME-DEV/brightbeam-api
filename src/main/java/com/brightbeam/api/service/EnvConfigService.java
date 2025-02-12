package com.brightbeam.api.service;

import org.springframework.stereotype.Service;

@Service
public class EnvConfigService {

    public String getDublinTreePath() {
        return System.getenv("DUBLIN_TREE_PATH");
    }

    public String getDublinPropertyPath() {
        return System.getenv("DUBLIN_PROPERTY_PATH");
    }
}

