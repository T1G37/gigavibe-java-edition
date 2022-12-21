package Bots.commands;

import Bots.BaseCommand;
import Bots.MessageEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Objects;

import static Bots.Main.createQuickEmbed;
import static Bots.Main.createQuickError;

public class CommandSendAnnouncement implements BaseCommand {

    @Override
    public void execute(MessageEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != 211789389401948160L) {
            event.getChannel().asTextChannel().sendMessageEmbeds(createQuickError("You dont have the permission to run this command.")).queue();
            return;
        }
        if (event.getArgs().length < 2) {
            event.getChannel().asTextChannel().sendMessageEmbeds(createQuickError("No argument given.")).queue();
            return;
        }
        for (int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            Objects.requireNonNull(event.getJDA().getGuilds().get(i).getDefaultChannel()).asTextChannel().sendMessageEmbeds(createQuickEmbed("**Announcement**", event.getContentRaw().replace(event.getArgs()[0], ""))).queue();
            try {
                Thread.sleep(10000);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public String getCategory() {
        return "dev";
    }

    @Override
    public String[] getNames() {
        return new String[]{"sendannouncement", "announcement", "announce"};
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[]{
                new OptionData(OptionType.STRING,"message","The announcement to send",true)
        };
    }

    @Override
    public String getDescription() {
        return "sends an announcement globally.";
    }
}