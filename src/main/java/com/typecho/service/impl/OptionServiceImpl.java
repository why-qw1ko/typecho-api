package com.typecho.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.typecho.entity.Option;
import com.typecho.mapper.OptionMapper;
import com.typecho.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionMapper optionMapper;

    @Override
    public Option getByName(String name) {
        return optionMapper.selectOne(
                new LambdaQueryWrapper<Option>()
                        .eq(Option::getName, name)
                        .eq(Option::getUser, 0)
        );
    }

    @Override
    public String getValue(String name) {
        Option option = getByName(name);
        return option != null ? option.getValue() : null;
    }

    @Override
    public Map<String, String> getAllOptions() {
        List<Option> options = optionMapper.selectList(
                new LambdaQueryWrapper<Option>().eq(Option::getUser, 0)
        );
        Map<String, String> result = new HashMap<>();
        for (Option option : options) {
            result.put(option.getName(), option.getValue());
        }
        return result;
    }

    @Override
    public List<Option> listByUser(Integer user) {
        return optionMapper.selectList(
                new LambdaQueryWrapper<Option>().eq(Option::getUser, user)
        );
    }

    @Override
    public void setOption(String name, String value) {
        Option option = getByName(name);
        if (option != null) {
            option.setValue(value);
            optionMapper.updateById(option);
        } else {
            option = new Option();
            option.setName(name);
            option.setUser(0);
            option.setValue(value);
            optionMapper.insert(option);
        }
    }

    @Override
    public void setOptions(Map<String, String> options) {
        for (Map.Entry<String, String> entry : options.entrySet()) {
            setOption(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void deleteOption(String name) {
        optionMapper.delete(
                new LambdaQueryWrapper<Option>()
                        .eq(Option::getName, name)
                        .eq(Option::getUser, 0)
        );
    }
}
