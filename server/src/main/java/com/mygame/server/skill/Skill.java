package com.mygame.server.skill;

import com.mygame.server.model.*;


public interface Skill { String getName(); void use(Player player, Monster monster); }