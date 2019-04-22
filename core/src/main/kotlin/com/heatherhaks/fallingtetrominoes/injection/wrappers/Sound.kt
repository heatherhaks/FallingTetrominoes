package com.heatherhaks.fallingtetrominoes.injection.wrappers

import com.badlogic.gdx.Gdx
import com.heatherhaks.fallingtetrominoes.clamp

class Sound {
    var music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"))
//    var musicVolume = 0.25f
    var musicVolume = 0f
        set(value) {
            field = value.clamp(0f, 1f)
            music.volume = field
        }
}