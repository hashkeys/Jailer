/*
 * Copyright 2007 - 2023 Ralf Wisser.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jailer.ui.databrowser.metadata;

import java.awt.Frame;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.sf.jailer.ExecutionContext;
import net.sf.jailer.database.Session;
import net.sf.jailer.datamodel.Association;
import net.sf.jailer.datamodel.DataModel;
import net.sf.jailer.datamodel.Table;
import net.sf.jailer.modelbuilder.MemorizedResultSet;
import net.sf.jailer.ui.DbConnectionDialog;
import net.sf.jailer.ui.QueryBuilderDialog;
import net.sf.jailer.ui.QueryBuilderDialog.Relationship;
import net.sf.jailer.ui.databrowser.BrowserContentPane;
import net.sf.jailer.ui.databrowser.BrowserContentPane.LoadJob;
import net.sf.jailer.ui.databrowser.BrowserContentPane.RowsClosure;
import net.sf.jailer.ui.databrowser.Desktop;
import net.sf.jailer.ui.databrowser.Desktop.FindClosureContext;
import net.sf.jailer.ui.databrowser.Desktop.RowBrowser;
import net.sf.jailer.ui.databrowser.Desktop.RunnableWithPriority;
import net.sf.jailer.ui.databrowser.Row;
import net.sf.jailer.ui.databrowser.sqlconsole.SQLConsole;
import net.sf.jailer.util.Pair;

/**
 * Renders content of {@link ResultSet}.
 * 
 * @author Ralf Wisser
 */
public class ResultSetRenderer extends javax.swing.JPanel {

    /**
     * Creates new form ResultSetRenderer
     * @param executionContext
     */
    public ResultSetRenderer(ResultSet resultSet, String titel, DataModel datamodel, Session session, ExecutionContext executionContext) throws SQLException {
        initComponents();
        if (titel != null && titel.length() > 40) {
            titelLabel.setText(titel.substring(0, 40) + "...");
        	titelLabel.setToolTipText(titel);
        } else {
        	titelLabel.setText(titel);
        	titelLabel.setToolTipText(null);
        }
        
		final BrowserContentPane rb = new ResultContentPane(datamodel, null, "", session, null,
                null, null, new RowsClosure(), Integer.MAX_VALUE, false, false,
                executionContext);
		if (resultSet instanceof MemorizedResultSet && ((MemorizedResultSet) resultSet).getSize() > 1) {
			rb.setTableFilterEnabled(true);
		} else {
			rb.setTableFilterEnabled(false);
		}
        LoadJob loadJob = rb.newLoadJob(resultSet, Integer.MAX_VALUE);
        loadJob.run();
        JComponent rTabContainer = rb.getRowsTableContainer();
        rb.sortColumnsCheckBox.setVisible(false);
        rb.sortColumnsPanel.setVisible(false);
        renderPanel.add(rTabContainer);
        rb.resetRowsTableContainer();
        renderPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titelLabel = new javax.swing.JLabel();
        renderPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        titelLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(titelLabel, gridBagConstraints);

        renderPanel.setLayout(new javax.swing.BoxLayout(renderPanel, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(renderPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jSeparator1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JLabel titelLabel;
    // End of variables declaration//GEN-END:variables
    class ResultContentPane extends BrowserContentPane {
    	private final Integer limit;
    	public ResultContentPane(DataModel dataModel, Table table, String condition, Session session,
                List<Row> parentRows, Association association, Frame parentFrame,
                RowsClosure rowsClosure, Integer limit, Boolean selectDistinct,
                boolean reload, ExecutionContext executionContext) {
            super(dataModel, table, condition, session, parentRows, association, parentFrame, 
            		rowsClosure, selectDistinct, reload, executionContext);
            singleRowDetailsViewTitel = "Details";
            useClassicSingleRowDetailsView = true;
            this.limit = limit;
            rowsTableScrollPane.setWheelScrollingEnabled(true);
            statusPanel.setVisible(false);
    	}
    	@Override
		protected double getAnimationFactor() {
			return 0.0;
		}
		@Override
        protected int getReloadLimit() {
        	if (limit == null) {
        		return Integer.MAX_VALUE;
        	}
        	return limit;
        }
		@Override
    	protected boolean useWhereClauseEditor() {
    		return false;
    	}
		@Override
		protected void setReloadLimit(int limit) {
		}
        @Override
        protected void unhide() {
        }
        @Override
        protected void showInNewWindow() {
        }
        @Override
        protected void reloadDataModel() throws Exception {
        }
        @Override
        protected void openSchemaMappingDialog() {
        }
        @Override
        protected void openSchemaAnalyzer() {
        }
        @Override
        protected void onRedraw() {
            ResultSetRenderer.this.repaint();
        }
        @Override
        protected void onHide() {
        }
        @Override
        protected void onContentChange(List<Row> rows, boolean reloadChildren) {
        }
        @Override
        protected RowBrowser navigateTo(Association association, List<Row> pRows) {
        	return null;
        }
        @Override
        protected List<RowBrowser> getTableBrowser() {
            return null;
        }
        @Override
        protected PriorityBlockingQueue<RunnableWithPriority> getRunnableQueue() {
            return Desktop.runnableQueue;
        }
        @Override
        protected QueryBuilderDialog getQueryBuilderDialog() {
            return null;
        }
        @Override
        protected RowBrowser getParentBrowser() {
            return null;
        }
        @Override
        protected JFrame getOwner() {
            Window owner = SwingUtilities.getWindowAncestor(ResultSetRenderer.this);
            if (owner instanceof JFrame) {
                return (JFrame) owner;
            }
            return null;
        }
        @Override
        protected double getLayoutFactor() {
            return 0;
        }
        @Override
        protected DbConnectionDialog getDbConnectionDialog() {
            return null;
        }
        @Override
        protected List<RowBrowser> getChildBrowsers() {
            return new ArrayList<RowBrowser>();
        }
        @Override
        protected void findClosure(Row row, Set<Pair<BrowserContentPane, Row>> closure, boolean forward, FindClosureContext findClosureContext) {
        }
        @Override
        protected void findClosure(Row row) {}
        @Override
        protected Relationship createQBRelations(boolean withParents) {
            return null;
        }
        @Override
        protected List<Relationship> createQBChildrenRelations(RowBrowser tabu, boolean all) {
            return null;
        }
        @Override
        protected void collectPositions(Map<String, Map<String, double[]>> positions) {
        }
        @Override
        protected void close() {
        }
        @Override
        protected void beforeReload() {
        }
        @Override
        protected void appendLayout() {
        }
        @Override
        protected void adjustClosure(BrowserContentPane tabu, BrowserContentPane thisOne) {
        }
        @Override
        protected void addRowToRowLink(Row pRow, Row exRow) {
        }
        @Override
        protected boolean renderRowAsPK(Row theRow) {
            return false;
        }
        @Override
        protected MetaDataSource getMetaDataSource() {
            return null;
        }
        @Override
        protected SQLConsole getSqlConsole(boolean switchToConsole) {
            return null;
        }
		@Override
		protected void deselectChildrenIfNeededWithoutReload() {
		}
		@Override
		protected void forceRepaint() {
		}
    }

}
