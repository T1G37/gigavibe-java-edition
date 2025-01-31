package Bots.commands;

import Bots.BaseCommand;
import Bots.MessageEvent;
import Bots.lavaplayer.GuildMusicManager;
import Bots.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.List;

import static Bots.Main.*;

public class CommandInsert extends BaseCommand {
    @Override
    public void execute(MessageEvent event) {
        if (!IsDJ(event.getGuild(), event.getChannel(), event.getMember())) {
            return;
        }
        if (IsChannelBlocked(event.getGuild(), event.getChannel())) {
            return;
        }
        String[] args = event.getContentRaw().split(" ", 3);
        // check here to ensure args[2] is never undefined.
        if (args.length != 3) {
            event.replyEmbeds(createQuickError("Not enough arguments provided\nUsage: `<Integer> <URL/SearchTerm>`"));
            return;
        }
        if (!args[1].matches("^\\d+$")) {
            event.replyEmbeds(createQuickError("Invalid arguments, integers only\nUsage: `<Integer> <URL/SearchTerm>`"));
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        List<AudioTrack> queue = new ArrayList<>(musicManager.scheduler.queue);
        List<String> tracksToPlay = new ArrayList<>();
        // prioritise attachment.
        if (!event.getAttachments().isEmpty()) {
            for (Message.Attachment attachment : event.getAttachments()) {
                tracksToPlay.add(attachment.getUrl());
            }
        }
        // if no attachment, try find url.
        else if (args[2].contains("https://") || args[2].contains("http://")) {
            if (args[2].contains("youtube.com/shorts/")) {
                args[2] = args[2].replace("youtube.com/shorts/", "youtube.com/watch?v=");
            }
            if (args[2].contains("youtu.be/")) {
                args[2] = args[2].replace("youtu.be/", "www.youtube.com/watch?v=");
            }
        } else {
            args[2] = "ytsearch: " + args[2];
        }
        tracksToPlay.add(args[2]);
        boolean sendEmbedBool = true;
        if (Integer.parseInt(args[1]) >= queue.size() || queue.isEmpty()) {
            for (String track : tracksToPlay) {
                try {
                    PlayerManager.getInstance().loadAndPlay(event.getChannel(), track, sendEmbedBool);
                    sendEmbedBool = false;
                } catch (FriendlyException ignored) {
                    event.replyEmbeds(createQuickError("Something went wrong when decoding the track."));
                }
            }
        } else {
            try {
                musicManager.scheduler.queue.clear();
                musicManager.scheduler.queue.addAll(queue.subList(0, Integer.parseInt(args[1]) - 1));
                for (String track : tracksToPlay) {
                    PlayerManager.getInstance().loadAndPlay(event.getChannel(), track, sendEmbedBool);
                    sendEmbedBool = false;
                }
                musicManager.scheduler.queue.addAll(queue.subList(Integer.parseInt(args[1]), queue.size()));
                event.replyEmbeds(createQuickEmbed(" ", "Added the track to position **" + args[1] + "**"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void ProvideOptions(SlashCommandData slashCommand) {
        slashCommand.addOptions(
                new OptionData(OptionType.INTEGER, "position", "Position to insert the track.", true),
                new OptionData(OptionType.STRING, "track", "The track to insert.", true)
        );
    }

    @Override
    public String getCategory() {
        return Categories.DJ.name();
    }

    @Override
    public String getOptions() {
        return "<Queue_Position> <Track>";
    }

    @Override
    public String[] getNames() {
        return new String[]{"insert"};
    }

    @Override
    public String getDescription() {
        return "Inserts a track into the queue.";
    }

    @Override
    public long getRatelimit() {
        return 2500;
    }
}