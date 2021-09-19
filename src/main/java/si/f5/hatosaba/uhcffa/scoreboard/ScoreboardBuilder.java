package si.f5.hatosaba.uhcffa.scoreboard;

import si.f5.hatosaba.uhcffa.user.User;

public class ScoreboardBuilder {

    private final User user;
    private Scoreboard board;

    public ScoreboardBuilder(User user) {
        this.user = user;
    }

    public void loadScoreboard() {
        clearScoreboard();

        board = new Scoreboard(user);
        board.setDisplay(true);
    }

    public void clearScoreboard(){
        if(board == null) return;

        board.setDisplay(false);

        board = null;
    }

    public Scoreboard getBoard() {
        return board;
    }
}
