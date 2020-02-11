package com.vtmer.yisanbang.service;


import com.vtmer.yisanbang.dto.SuitDto;

import java.util.List;

public interface SuitService {
    // 查找所有套装
    public List<SuitDto> selectAll();

    // 根据套装id删除套装
    public boolean deleteSuitById(Integer suitId);

    // 根据套装id更新套装
    public boolean updateSuitById(SuitDto suitDto);

    // 添加套装
    public boolean addSuit(SuitDto suitDto);

    // 判断套装是否已经存在
    public boolean judgeSuit(SuitDto suitDto, List<SuitDto> suitDtoList);

    // 根据套装id查找套装
    public SuitDto selectSuitById(Integer suitId);

    // 根据套装名字查找套装
    public SuitDto selectSuitByName(String suitName);

    // 根据套装的最低价格排序进行显示
    public List<SuitDto> selectSuitOrderByPrice();

    // 根据套装的时间排序进行显示
    public List<SuitDto> selectSuitOrderByTime();

    // 根据分类id显示套装
    public List<SuitDto> selectSuitBySort(Integer sortId);

    // 隐藏套装
    public boolean hideSuit(SuitDto suitDto);
}
