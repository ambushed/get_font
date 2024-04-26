package company.ui;

import company.PlatformTextField;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;


public class ProductsVolumeUI {

    private JPanel mainPanel;
    private JXTable table;

    private JPanel exchangesPanel;
    private JFrame filtersWindowFrame;

    private JLabel dcLabel;
    private JButton sendPositionsMailBtn;
    private JCheckBox diffCheckBox;
    private JPanel adminPanel;
    private JButton addManualDealBtn;
    private JCheckBox enableAddDealCB;
    private JComboBox<Integer> dealActionCombo;
    private JComboBox<Integer> dealTradableTypeCombo;
    private JComboBox<Integer> dealSourceCombo;
    private JLabel exchangeCodeDealLabel;
    private JButton btnReport;
    private JButton FiltersBtn;
    private PlatformTextField<Integer> dealAmountTF;
    private PlatformTextField<Float> dealPriceTF;
    private PlatformTextField<String> dealTradableIdTF;
    private PlatformTextField<String> dealUserOrPortfolioTF;

    private final TabType tabType;

    /**
     * The type of tab
     */
    public static enum TabType {
        DERIVATIVES, STOCKS, COUNTER
    }

    /**
     * The location of the previous window created
     */
    final Point pointOfPreviousWindow;
    public ProductsVolumeUI(final TabType tabType, final Point pointOfMainWindow) {
        this.tabType = tabType;
        this.pointOfPreviousWindow = pointOfMainWindow;
    }

    public JPanel getMainPanel() {return mainPanel;}

}
