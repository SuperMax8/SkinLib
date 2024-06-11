package fr.supermax_8.skinlib;

import lombok.Getter;
import org.mineskin.data.Skin;

import java.util.HashMap;

@Getter
public class SkinCache {

    private final HashMap<String, Skin> cache = new HashMap<>();


}