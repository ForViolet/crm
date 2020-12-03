package com.ly.workbench.service;

import com.ly.workbench.domain.Clue;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);
}
