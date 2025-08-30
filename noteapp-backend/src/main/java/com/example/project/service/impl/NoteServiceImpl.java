package com.example.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.entity.Note;
import com.example.project.mapper.NoteMapper;
import com.example.project.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.project.common.ex.BusinessException;

import java.util.List;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    
    @Autowired
    private NoteMapper noteMapper;
    
    @Override
    public List<Note> findByUserId(Long userId) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return noteMapper.selectList(queryWrapper);
    }
    
    @Override
    public Note findById(Long id) {
        return noteMapper.selectById(id);
    }
    
    @Override
    public Note create(Note note) {
        try {
            int result = noteMapper.insert(note);
            if (result > 0) {
                return note;
            } else {
                throw new BusinessException("创建笔记失败");
            }
        } catch (Exception e) {
            throw new BusinessException("创建笔记时发生错误: " + e.getMessage());
        }
    }
    
    @Override
    public Note update(Note note) {
        noteMapper.updateById(note);
        return note;
    }
    
    @Override
    public boolean delete(Long id) {
        return noteMapper.deleteById(id) > 0;
    }
}