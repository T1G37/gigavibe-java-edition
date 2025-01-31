package Bots.commands;

import Bots.BaseCommand;
import Bots.MessageEvent;

import static Bots.Main.createQuickEmbed;

public class CommandPrivacyPolicy extends BaseCommand {

    @Override
    public void execute(MessageEvent event) throws Exception {
        event.replyEmbeds(createQuickEmbed("Privacy Policy", "https://github.com/ZeNyfh/gigavibe-java-edition/blob/main/PRIVACY_POLICY.md"));
    }

    @Override
    public String[] getNames() {
        return new String[]{"privacypolicy", "privacy policy", "pp"};
    }

    @Override
    public String getCategory() {
        return Categories.General.name();
    }

    @Override
    public String getDescription() {
        return "Sends a link to the privacy policy.";
    }

    @Override
    public long getRatelimit() {
        return 10000;
    }
}
