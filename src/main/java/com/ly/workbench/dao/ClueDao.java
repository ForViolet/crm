package com.ly.workbench.dao;

import com.ly.workbench.domain.Clue;

public interface ClueDao {

    int save(Clue clue);

    Clue detail(String id);
}
