package Bots.commands;

import Bots.BaseCommand;
import Bots.MessageEvent;
import Bots.lavaplayer.GuildMusicManager;
import Bots.lavaplayer.PlayerManager;

import static Bots.Main.*;

public class CommandDisconnect extends BaseCommand {
    @Override
    public void execute(MessageEvent event) {
        if (!IsDJ(event.getGuild(), event.getChannel().asTextChannel(), event.getMember())) {
            return;
        }
        if (!event.getGuild().getAudioManager().isConnected()) {
            event.getChannel().asTextChannel().sendMessageEmbeds(createQuickError("I am not in a voice channel.")).queue();
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        musicManager.scheduler.queue.clear();
        event.getGuild().getAudioManager().closeAudioConnection();
        musicManager.scheduler.nextTrack();
        event.getChannel().asTextChannel().sendMessageEmbeds(createQuickEmbed(" ", "✅ Disconnected from the voice channel and cleared the queue.")).queue();
        clearVotes(event.getGuild().getIdLong());
    }

    @Override
    public String[] getNames() {
        return new String[]{"disconnect", "fu" + "ckoff", "fu" + "ck off", "shutup", "dc", "stop", "leave"};
    }

    @Override
    public String getCategory() {
        return "DJ";
    }

    @Override
    public String getDescription() {
        return "Makes the bot forcefully leave the vc.";
    }

    @Override
    public long getRatelimit() {
        return 5000;
    }
}
