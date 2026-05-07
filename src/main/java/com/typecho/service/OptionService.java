package com.typecho.service;

import com.typecho.entity.Option;

import java.util.List;
import java.util.Map;

public interface OptionService {
    Option getByName(String name);

    String getValue(String name);

    Map<String, String> getAllOptions();

    List<Option> listByUser(Integer user);

    void setOption(String name, String value);

    void setOptions(Map<String, String> options);

    void deleteOption(String name);
}
