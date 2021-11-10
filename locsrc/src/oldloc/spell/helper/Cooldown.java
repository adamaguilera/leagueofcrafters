package oldloc.spell.helper;

import oldloc.messager.Chat;
import oldloc.player.User;
import lombok.Builder;

@Builder
public class Cooldown {
    // cooldown for sending messages to avoid spam
    private final int MESSAGE_COOLDOWN = 250;

    final User user;
    final String name;
    final String cdMessage;
    final String activeMessage;
    final long cdMillis;


    // last time the ability was cast
    private long lastCast;
    // last time a cooldown message was sent
    private long lastCooldownMessage;
    private Chat chat;

    public Chat getChat () {
        if (chat == null) {
            this.chat = new Chat();
        }
        return this.chat;
    }

    /**
     * @return next time the ability is off cooldown in milliseconds
     */
    public long getNextCast () {
        return lastCast + cdMillis;
    }

    /**
     * SIDE EFFECT NOTICE THAT THIS WILL SEND COOLDOWN MESSAGE IF NOT NULL
     * @return whether the ability is on cooldown
     */
    public boolean onCooldown () {
        long currentTime = System.currentTimeMillis();
        long nextCast = getNextCast();
        if (nextCast > currentTime) {
            // send cooldown message if it exists
            sendCooldownMessage ();
            return true;
        } else {
            return false;
        }
    }

    /**
     * sends cooldown message to the player
     */
    private void sendCooldownMessage () {
        if (cdMessage != null) {
            // check if not on cooldown
            if (lastCooldownMessage + MESSAGE_COOLDOWN <= System.currentTimeMillis()) {
                // update lastCooldownMessage
                lastCooldownMessage = System.currentTimeMillis();
                // send message
                // get the cooldown remaining
                double cooldownRemaining = (getNextCast() - System.currentTimeMillis()) / 1000.0;
                // possible bug if .replace all has side effect ??
                String message = cdMessage.replaceAll("_", String.format("%.2f", cooldownRemaining));
                getChat().ability(user, message);
            }
        }
    }

    /**
     * Sets the last cast equal to the current time
     */
    public void putOnCooldown () {
        lastCooldownMessage = System.currentTimeMillis();
        lastCast = System.currentTimeMillis();
    }
    /**
     * Sets the last time to a specific time
     * @param time the time to set
     */
    public void setLastCast (long time) {
        lastCast = time;
    }

    public void removeCooldown () {
        lastCast = Long.MIN_VALUE;
    }

}
