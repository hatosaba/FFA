package si.f5.hatosaba.uhcffa.schedule.task;

import si.f5.hatosaba.uhcffa.user.UserSet;

public class UpdateHealth extends AsyncTask {

    public UpdateHealth() {
        super(1);
    }

    @Override
    public void run() {
        UserSet.getInstnace().getOnlineUsers().stream().forEach(user -> {
            if (user.scoreboardBuilder != null) {

                user.scoreboardBuilder.getBoard().updatelife();
            }
        });
    }

}
