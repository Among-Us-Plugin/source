package io.papermc.aup.classes;

public class Vote {
    
    private AmongUsPlayer voter;
    private AmongUsPlayer recipient;

    public Vote(AmongUsPlayer voter, AmongUsPlayer recipient) {
        this.voter = voter;
        this.recipient = recipient;
    }

    public AmongUsPlayer getVoter() {
        return voter;
    }

    public AmongUsPlayer getRecipient() {
        return recipient;
    }

}
