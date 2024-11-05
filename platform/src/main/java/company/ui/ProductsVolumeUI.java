package company.ui;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;


public class ProductsVolumeUI {

    private JPanel mainPanel;
    private JXTable table;

    private JPanel exchangesPanel;
    private JFrame filtersWindowFrame;

    private JLabel dcLabel;
    private JButton sendPositionsMailBtn;
    private JCheckBox diffCheckBox;
    private JPanel adminPanel;
    private JLabel exchangeCodeDealLabel;
    private JButton btnReport;
    private JButton FiltersBtn;



    /**
     * The location of the previous window created
     */
    final Point pointOfPreviousWindow;
    public ProductsVolumeUI(final Point pointOfMainWindow) {
        this.pointOfPreviousWindow = pointOfMainWindow;
    }

    public JPanel getMainPanel() {return mainPanel;}

}
