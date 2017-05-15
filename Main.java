import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentListener;
import java.awt.event.*;

public class Main {

    /*
     * Example main method
     * You can use this main as a playground to test the model with your API key
     * Again, the API key can be obtained by following this guide:
     * https://developers.google.com/youtube/registering_an_application#create_project
     */ 
    public static void main(String[] args) {
        ModelYouTube youtubeModel = new ModelYouTube("AIzaSyBG__2uvsC9rOzfJs5xL0BstOfAXHN0SDc");
        //youtubeModel.searchVideos("puppies");

        JFrame frame = new JFrame("Youtube Video Gallery");
        Model model = new Model(frame, youtubeModel);
        JFrame top_frame = new JFrame("Youtube Video Gallery Menu Bar");

        frame.setTitle("Youtube Video Gallery");
        frame.setSize(1400, 800);
        frame.setMinimumSize(new Dimension(500, 450));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);

        top_frame.setTitle("Youtube Video Gallery Menu Bar");
        top_frame.setSize(1066, 100);
        top_frame.setMinimumSize(new Dimension(1066, 100));
        top_frame.setBackground(Color.WHITE);
        top_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        MenuBarView top = new MenuBarView(model);
        top.setSize(1400, 75);
        CenterView center = new CenterView(model);
        center.setSize(new Dimension(1400, 800));
        JScrollPane center_scroll = new JScrollPane(center);
        center_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        center_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(top, BorderLayout.NORTH);
        frame.add(center_scroll, BorderLayout.CENTER);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (frame.getWidth() <= 840) {
                    model.setMax_cols(1);
                } else if (frame.getWidth() <= 1240) {
                    model.setMax_cols(2);
                } else {
                    model.setMax_cols(3);
                }

                if (model.getIs_grid()) {
                    frame.setMinimumSize(new Dimension(630, 450));
                } else {
                    frame.setMinimumSize(new Dimension(630, 350));
                }
                model.update();
            }
        });

        model.notifyObservers();
        frame.setVisible(true);
    }

}