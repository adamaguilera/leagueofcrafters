package oldloc.player.managers;

import oldloc.Main;
import oldloc.game.Game;
import oldloc.messager.Chat;
import oldloc.player.Hero;
import oldloc.player.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LobbyManager {
    final String READY_MESSAGE = "You are now ready!";
    final String UNREADY_MESSAGE = "You are no longer ready!";
    final String NOT_IN_LOBBY = "You can only do this in the lobby!";
    final String GAME_STARTED = "The game has already started!";
    final String GAME_COMPLETED = "The game has already finished!";
    final Chat chat = new Chat();
    final User user;
    @Getter
    boolean isReady = false;


    public Game getGame () {
        return Main.GET_GAME();
    }

    public boolean ready () {
        isReady = !isReady;
        if (getGame().getState() == Game.GameState.LOBBY) {
            chat.command(user, isReady ? READY_MESSAGE : UNREADY_MESSAGE);
            if (allReady()) {
                getGame().start();
            }
        } else {
            chat.command(user, NOT_IN_LOBBY);
        }
        return isReady;
    }

    public boolean allReady () {
        return getGame().getUserManager().getUsers().stream()
                .map(User::getHero).map(Hero::getLobbyManager)
                .allMatch(LobbyManager::isReady);
    }
}
