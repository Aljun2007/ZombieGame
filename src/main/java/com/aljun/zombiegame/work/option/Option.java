package com.aljun.zombiegame.work.option;

import com.aljun.zombiegame.work.datamanager.datamanager.LevelDataManager;
import com.aljun.zombiegame.work.eventsubscriber.option.OptionRegisterEvent;
import net.minecraft.server.level.ServerLevel;

public class Option<T> {
    public final OptionManager.RegisterPack<T> REGISTER_PACK;
    private boolean isRegistered = false;

    public Option(OptionManager.RegisterPack<T> registerPack) {
        REGISTER_PACK = registerPack;
    }

    public void register() {
        if (!OptionRegisterEvent.isRegistered()) {
            OptionManager.register(this.REGISTER_PACK);
            this.isRegistered = true;
        } else {
            throw new RuntimeException("OptionRegisterError!Reason: You can't REGISTER it at this moment");
        }
    }

    public T getValue(ServerLevel level) {
        if (!this.isRegistered) {
            throw new RuntimeException("OptionNotFind!Reason: Did not registered");
        }
        level = level.getServer().overworld();
        return LevelDataManager.getOrDefault(level, this.REGISTER_PACK.KEY_SET);
    }
}
