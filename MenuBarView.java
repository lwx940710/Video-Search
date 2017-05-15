import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Object;
import java.util.*;
import javax.swing.border.EmptyBorder;

public class MenuBarView extends JPanel implements Observer {

    private Model model;
    private JToolBar toolbar;
    private JPanel layout_panel, search_panel, sl_panel, filter_panel;
    private JButton grid, list;
    private JTextField text;
    private JButton search, save, load;
    private JButton star1, star2, star3, star4, star5;
    private JButton clear;

    private int width, height;
    /**
     * Create a new View.
     */
    public MenuBarView(Model model) {
        this.model = model;
        width = 800;
        height = 75;

        toolbar = new JToolBar("Youtube Video Gallery Tool Bar");
        toolbar.setSize(new Dimension(width, height));

        layout_panel = new JPanel();
        GridLayout gl = new GridLayout(1, 2);
        gl.setHgap(-5);
        gl.setVgap(0);
        layout_panel.setLayout(gl);
        layout_panel.setBackground(Color.WHITE);
        grid = new JButton(new ImageIcon("Grid.png"));
        grid.setSelected(true);
        grid.setPreferredSize(new Dimension(width / 20, 45));
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.setSelected(false);
                grid.setSelected(true);
                model.setIs_grid(true);
                model.update();
            }
        });
        list = new JButton(new ImageIcon("List.png"));
        list.setSelected(false);
        list.setPreferredSize(new Dimension(width / 20, 45));
        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.setSelected(false);
                list.setSelected(true);
                model.setIs_grid(false);
                model.update();
            }
        });
        layout_panel.add(grid, BorderLayout.WEST);
        layout_panel.add(list, BorderLayout.EAST);

        search_panel = new JPanel();
        search_panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        search_panel.setBackground(Color.WHITE);
        text = new JTextField();
        text.setColumns(width / 40);
        search = new JButton(new ImageIcon("Search.png"));
        search.setPreferredSize(new Dimension(width / 20, 45));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clearAll();
                model.setKeyword(text.getText());
                model.setLoading(true);
                search.setIcon(null);
                search.setText("Loading...");
                setAllBut(false);

                grid.paintImmediately(0, 0, width, 100);
                list.paintImmediately(0, 0, width, 100);
                text.paintImmediately(0, 0, width, 100);
                search.paintImmediately(0, 0, width, 100);
                save.paintImmediately(0, 0, width, 100);
                load.paintImmediately(0, 0, width, 100);
                star1.paintImmediately(0, 0, width, 100);
                star2.paintImmediately(0, 0, width, 100);
                star3.paintImmediately(0, 0, width, 100);
                star4.paintImmediately(0, 0, width, 100);
                star5.paintImmediately(0, 0, width, 100);
                clear.paintImmediately(0, 0, width, 100);

                model.searchVideos(text.getText());
                model.update();
            }
        });
        search_panel.add(text);
        search_panel.add(search, BorderLayout.WEST);

        sl_panel = new JPanel(new GridLayout(2, 1));
        sl_panel.setBackground(Color.WHITE);
        save = new JButton("Save");
        save.setPreferredSize(new Dimension(width / 15, 20));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.save()) {
                    JOptionPane.showMessageDialog(model.getFrame(), "Saved!");
                } else {
                    JOptionPane.showMessageDialog(model.getFrame(), "Save failed!");
                }
            }
        });
        load = new JButton("Load");
        load.setPreferredSize(new Dimension(width / 15, 20));
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getNum_results() > 0) {
                    int result = JOptionPane.showConfirmDialog(model.getFrame(),
                            "Do you want to save current result? Any unsaved changes will be lost", "Alert",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (!model.save()) {
                            JOptionPane.showMessageDialog(model.getFrame(), "Save failed!");
                            return;
                        }
                    }
                }
                if (model.load()) {
                    JOptionPane.showMessageDialog(model.getFrame(), "Loaded!");
                } else {
                    JOptionPane.showMessageDialog(model.getFrame(), "Load failed!");
                }

            }
        });
        sl_panel.add(save);
        sl_panel.add(load);

        //filter_panel = new JPanel(new GridLayout(1, 5));
        filter_panel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 15, 0));
        filter_panel.setBackground(Color.WHITE);
        filter_panel.setBorder(new EmptyBorder(15, 0, 0, 0));
        star1 = initial_star();
        star1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getStar() == 1) {
                    model.setStar(0);
                } else {
                    model.setStar(1);
                }
                model.paintStars(star1, star2, star3, star4, star5, model.getStar());
                model.update();
            }
        });
        star2 = initial_star();
        star2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getStar() == 2) {
                    model.setStar(0);
                } else {
                    model.setStar(2);
                }
                model.paintStars(star1, star2, star3, star4, star5, model.getStar());
                model.update();
            }
        });
        star3 = initial_star();
        star3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getStar() == 3) {
                    model.setStar(0);
                } else {
                    model.setStar(3);
                }
                model.paintStars(star1, star2, star3, star4, star5, model.getStar());
                model.update();
            }
        });
        star4 = initial_star();
        star4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getStar() == 4) {
                    model.setStar(0);
                } else {
                    model.setStar(4);
                }
                model.paintStars(star1, star2, star3, star4, star5, model.getStar());
                model.update();
            }
        });
        star5 = initial_star();
        star5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getStar() == 5) {
                    model.setStar(0);
                } else {
                    model.setStar(5);
                }
                model.paintStars(star1, star2, star3, star4, star5, model.getStar());
                model.update();
            }
        });
        clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                star1.setIcon(new ImageIcon("Star_unselected.png"));
                star2.setIcon(new ImageIcon("Star_unselected.png"));
                star3.setIcon(new ImageIcon("Star_unselected.png"));
                star4.setIcon(new ImageIcon("Star_unselected.png"));
                star5.setIcon(new ImageIcon("Star_unselected.png"));
                model.setStar(0);
                model.update();
            }
        });
        filter_panel.add(star1);
        filter_panel.add(star2);
        filter_panel.add(star3);
        filter_panel.add(star4);
        filter_panel.add(star5);
        filter_panel.add(clear);

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 5));

        toolbar.add(layout_panel);
        toolbar.add(search_panel);
        toolbar.add(sl_panel);
        toolbar.add(filter_panel);
        this.add(toolbar, BorderLayout.PAGE_START);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updateButtonSize();
                repaint();
            }
        });

        model.addObserver(this);
    }

    private JButton initial_star() {
        JButton button = new JButton(new ImageIcon("Star_unselected.png"));
        button.setPreferredSize(new Dimension(24, 24));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        //button.setBorder(new LineBorder(Color.YELLOW, 2));
        return button;
    }

    public void updateButtonSize() {
        width = getWidth();
        height = getHeight();

        //System.out.println("width = " + width + "  height = " + height);

        grid.setPreferredSize(new Dimension(width / 20, 45));
        list.setPreferredSize(new Dimension(width / 20, 45));

        text.setColumns(width / 40);
        search.setPreferredSize(new Dimension(width / 20, 45));

        save.setPreferredSize(new Dimension(width / 15, 20));
        load.setPreferredSize(new Dimension(width / 15, 20));

        star1.setPreferredSize(new Dimension(24, 24));
        star2.setPreferredSize(new Dimension(24, 24));
        star3.setPreferredSize(new Dimension(24, 24));
        star4.setPreferredSize(new Dimension(24, 24));
        star5.setPreferredSize(new Dimension(24, 24));
    }

    public void setAllBut(Boolean b) {
//        private JButton grid, list;
//        private JTextField text;
//        private JButton search, save, load;
//        private JButton star1, star2, star3, star4, star5;
//        private JButton clear;
        grid.setEnabled(b);
        list.setEnabled(b);
        text.setEnabled(b);
        search.setEnabled(b);
        save.setEnabled(b);
        load.setEnabled(b);
        star1.setEnabled(b);
        star2.setEnabled(b);
        star3.setEnabled(b);
        star4.setEnabled(b);
        star5.setEnabled(b);
        clear.setEnabled(b);
    }

    /**
     * Update with data from the model.
     */
    public void update(Observable o, Object obj) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.

        if (!model.getLoading()) {
            search.setText("");
            search.setIcon(new ImageIcon("Search.png"));
            setAllBut(true);
        }
        model.paintStars(star1, star2, star3, star4, star5, model.getStar());
        text.setText(model.getKeyword());
        repaint();
    }
}
