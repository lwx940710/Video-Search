import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

/**
 * Created by lwx on 2017-02-28.
 */
public class CenterView extends JPanel implements Observer {
    private Model model;
    private int width, height;
    private ArrayList<JPanel> panels;
    private JPanel loading_panel;

    CenterView(Model model) {
        this.model = model;
        width = this.getWidth();
        height = this.getHeight();

        panels = new ArrayList<JPanel>(25);
        loading_panel = new JPanel();


        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(new EmptyBorder(10, 40, 10, 40));

        JPanel flow_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 80, 40));
        flow_panel.setBackground(Color.WHITE);
        for (int i = 0; i < 25; i++) {
            JPanel p = new JPanel();
            p.setBackground(Color.WHITE);
            panels.add(i, p);
            if (i % model.getMax_cols() == 0 && i >= model.getMax_cols()) {
                this.add(flow_panel);
                flow_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 80, 40));
                flow_panel.setBackground(Color.WHITE);
                flow_panel.add(panels.get(i));
            } else {
                flow_panel.add(panels.get(i));
            }
        }
        this.add(flow_panel);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(e.getX() + " " + e.getY());
            }
        });

        model.addObserver(this);
        model.setCv(this);
    }

    private void showResult_grid() {
        this.removeAll();
        this.revalidate();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        int size = model.getNum_results();
//        int n = size / 3;
//        if  (size % 3 > 0) {
//            n += 1;
//        }
//        this.setLayout(new GridLayout(n, 3, 80, 40));
        int n = 0;
        JPanel flow_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 80, 40));
        flow_panel.setBackground(Color.WHITE);

        for (int i = 0; i < size; i++) {
            Model.Search_result sr = model.getSearch_results(i);
            if (sr.getRating() < model.getStar()) {
                continue;
            }

            GridLayout gl = new GridLayout(3, 1);
            gl.setVgap(-100);
            panels.get(i).setLayout(gl);

            JLabel stars_label = new JLabel();
            stars_label.setLayout(new FlowLayout(FlowLayout.TRAILING));
            JButton star1 = initial_star();
            JButton star2 = initial_star();
            JButton star3 = initial_star();
            JButton star4 = initial_star();
            JButton star5 = initial_star();
            star1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 1) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(1);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 2) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(2);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 3) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(3);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 4) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(4);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 5) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(5);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
            stars_label.add(star1);
            stars_label.add(star2);
            stars_label.add(star3);
            stars_label.add(star4);
            stars_label.add(star5);
            //System.out.println("after star_label");

            JLabel pic_label = new JLabel();
            pic_label.setLayout(new GridLayout(1, 1));
            pic_label.setBackground(Color.WHITE);
            JButton pic = new JButton(new ImageIcon(sr.getImg()));
            pic.setBorder(new LineBorder(Color.WHITE, 1));
            pic.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        URL url = new URL("http://youtube.com/watch?v=" + sr.getVideo_id());
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(url.toURI());
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            });
            pic_label.add(pic);

            JLabel description_label = new JLabel();
            GridLayout des_gl = new GridLayout(10, 1);
            description_label.setLayout(des_gl);
            JLabel title = new JLabel(sr.getTitle());
            JLabel time = new JLabel(sr.getRelative_date());
            description_label.add(new JLabel("         "));
            description_label.add(new JLabel("         "));
            description_label.add(new JLabel("         "));
            description_label.add(new JLabel("         "));
            description_label.add(new JLabel("         "));
            description_label.add(new JLabel("         "));
            description_label.add(new JLabel("         "));
            description_label.add(title);
            description_label.add(new JLabel("         "));
            description_label.add(time);


            panels.get(i).setPreferredSize(new Dimension(320, 250));
            panels.get(i).setBorder(new LineBorder(Color.BLACK, 2));
            panels.get(i).removeAll();
            panels.get(i).add(stars_label);
            panels.get(i).add(pic_label);
            panels.get(i).add(description_label);

            if (n % model.getMax_cols() == 0 && n >= model.getMax_cols()) {
                this.add(flow_panel);
                flow_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 80, 40));
                flow_panel.setBackground(Color.WHITE);
                flow_panel.add(panels.get(i));
            } else {
                flow_panel.add(panels.get(i));
            }
            n += 1;
        }
        this.add(flow_panel);
    }

    private void showResult_list() {
        this.removeAll();
        this.revalidate();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        int size = model.getNum_results();
        int n = 0;

        for (int i = 0; i < size; i++) {
            Model.Search_result sr = model.getSearch_results(i);
            if (sr.getRating() < model.getStar()) {
                continue;
            }

            GridLayout gl = new GridLayout(1, 2);
            panels.get(i).setLayout(gl);

            JLabel pic_label = new JLabel();
            pic_label.setLayout(new GridLayout(1, 1));
            pic_label.setBackground(Color.WHITE);
            JButton pic = new JButton(new ImageIcon(sr.getImg()));
            pic.setBorder(new LineBorder(Color.WHITE, 1));
            pic.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        URL url = new URL("http://youtube.com/watch?v=" + sr.getVideo_id());
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(url.toURI());
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            });
            pic_label.add(pic);

            JLabel stars_label = new JLabel();
            stars_label.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 0));
            JButton star1 = initial_star();
            JButton star2 = initial_star();
            JButton star3 = initial_star();
            JButton star4 = initial_star();
            JButton star5 = initial_star();
            star1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 1) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(1);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 2) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(2);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 3) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(3);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            star4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 4) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(4);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()){
                        model.update();
                    }
                }
            });
            star5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sr.getRating() == 5) {
                        sr.setRating(0);
                    } else {
                        sr.setRating(5);
                    }
                    model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
                    if (sr.getRating() < model.getStar()) {
                        model.update();
                    }
                }
            });
            model.paintStars(star1, star2, star3, star4, star5, sr.getRating());
            stars_label.add(star1);
            stars_label.add(star2);
            stars_label.add(star3);
            stars_label.add(star4);
            stars_label.add(star5);

            JLabel description_label = new JLabel();
            description_label.setLayout(new GridLayout(3, 1));
            JLabel title = new JLabel(sr.getTitle());
            JLabel time = new JLabel(sr.getRelative_date());
            description_label.add(title);
            description_label.add(new JLabel("         "));
            description_label.add(time);


            JLabel star_and_desc = new JLabel();
            star_and_desc.setLayout(new GridLayout(4, 1));
            star_and_desc.add(new JLabel("     "));
            star_and_desc.add(stars_label);
            star_and_desc.add(description_label);
            star_and_desc.add(new JLabel("     "));


            panels.get(i).setPreferredSize(new Dimension(600, 230));
            panels.get(i).setBorder(new LineBorder(Color.WHITE, 2));
            panels.get(i).removeAll();
            panels.get(i).revalidate();
            panels.get(i).add(pic_label);
            panels.get(i).add(star_and_desc);
            //panels.get(i).add(new JLabel("     "));
            this.add(panels.get(i));
            n += 1;
        }
        if (n == 1) {
            JPanel empty = new JPanel();
            empty.setBackground(Color.WHITE);
            this.add(empty);
        }
    }

    private void showLoading() {
        this.removeAll();
        this.revalidate();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        ImageIcon loading_icon = new ImageIcon("ajax-loader.gif");
        JLabel loading_label = new JLabel("loading...", loading_icon, JLabel.CENTER);
        JButton loading_but = new JButton(loading_icon);
        loading_panel = new JPanel(new GridLayout(1, 1));
        loading_panel.setBorder(new LineBorder(Color.RED, 10));
        loading_panel.setPreferredSize(new Dimension(600, 600));
        loading_panel.setSize(new Dimension(600, 600));
        loading_panel.add(loading_but);
        this.add(loading_panel);
    }

    private JButton initial_star() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(24, 24));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        return button;
    }


    /**
     * Update with data from the model.
     */
    public void update(Observable o, Object obj) {
        if (model.getLoading()) {
            showLoading();
        } else if (model.getIs_grid()) {
            showResult_grid();
        } else {
            showResult_list();
        }
        repaint();
    }
}