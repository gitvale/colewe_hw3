package uni.colewe.client;

import java.util.List;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;

import uni.colewe.shared.MyDictionaryEntry;

public class MyDictionaryWidget extends Composite {

	private static MyDictionaryWidgetUiBinder uiBinder = GWT.create(MyDictionaryWidgetUiBinder.class);

	interface MyDictionaryWidgetUiBinder extends UiBinder<Widget, MyDictionaryWidget> {
	}

	@UiField
	TextBox txtQuery;
	@UiField
	Button btnSearch;
	@UiField
	CheckBox chkRev;
	@UiField
	CheckBox chkLike;
	@UiField
	CheckBox chkHl;
	@UiField
	Label lblResponse;
	@UiField
	SimplePanel panel;

	CellTable<MyDictionaryEntry> tblResults = new CellTable<MyDictionaryEntry>();

	private final MyDictServiceAsync dictService = GWT.create(MyDictService.class);

	final ListDataProvider<MyDictionaryEntry> dataProvider = new ListDataProvider<MyDictionaryEntry>();

	public MyDictionaryWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		// set element text
		btnSearch.setText("Search");
		chkRev.setText("Search in English");
		chkLike.setText("Fuzzy matching");
		chkHl.setText("Highlight query in results");
		lblResponse.setText("");
		
		SafeHtmlCell safeCell = new SafeHtmlCell();
		Column<MyDictionaryEntry, SafeHtml> colRus = new Column<MyDictionaryEntry, SafeHtml>(safeCell)  {
			@Override
			public SafeHtml getValue(MyDictionaryEntry entry) {
				return SafeHtmlUtils.fromTrustedString(entry.getRus());
			}
		};
		tblResults.addColumn(colRus, "Russian");

		//final SafeHtmlCell posCell = new SafeHtmlCell();
		Column<MyDictionaryEntry, SafeHtml> colPos = new Column<MyDictionaryEntry, SafeHtml>(safeCell)  {
			@Override
			public SafeHtml getValue(MyDictionaryEntry entry) {
				return SafeHtmlUtils.fromString(entry.getPos());
			}
		};
		tblResults.addColumn(colPos, "POS");

		//SafeHtmlCell engCell = new SafeHtmlCell();
		Column<MyDictionaryEntry, SafeHtml> colEng = new Column<MyDictionaryEntry, SafeHtml>(safeCell)  {
			@Override
			public SafeHtml getValue(MyDictionaryEntry entry) {
				return SafeHtmlUtils.fromTrustedString(entry.getEng());
			}
		};
		tblResults.addColumn(colEng, "English");

		dataProvider.addDataDisplay(tblResults);
		panel.add(tblResults);
	}

	@UiHandler("btnSearch")
	void onSearch(ClickEvent e) {
		dictService.queryServer(txtQuery.getText(), chkRev.getValue(), chkLike.getValue(),
				new AsyncCallback<List<MyDictionaryEntry>>() {

					public void onFailure(Throwable caught) {
						lblResponse.setText("Search failed: " + caught.getMessage());
						lblResponse.setStyleName("error");
						clearTable();
					}

					public void onSuccess(List<MyDictionaryEntry> result) {
						lblResponse.setText(
								"Search successful. Found " + Integer.toString(result.size()) + " matching entries");
						lblResponse.setStyleName("success");
						toggleHighLighting(chkHl.getValue(), result);
						fillTable(result);
					}
				});
	}

	@UiHandler("chkHl")
	void onHighLight(ClickEvent e) {
		toggleHighLighting(chkHl.getValue(), dataProvider.getList());
		dataProvider.flush();
		dataProvider.refresh();
		tblResults.redraw();
	}

	@UiHandler("txtQuery")
	void onTextEntered(KeyPressEvent e) {
		if (chkHl.getValue()) {

		}
	}

	private void fillTable(List<MyDictionaryEntry> result) {
		dataProvider.getList().clear();
		dataProvider.getList().addAll(result);
		tblResults.setVisibleRange(new Range(0, result.size()));
		tblResults.setRowCount(result.size(), true);
		dataProvider.flush();
		dataProvider.refresh();
		tblResults.redraw();

	}

	private void clearTable() {
		dataProvider.getList().clear();
		tblResults.setRowCount(0, true);
		dataProvider.flush();
		dataProvider.refresh();
		tblResults.redraw();

	}

	private String highlight(String target) {
		if (chkLike.getValue()) {
			return target.replaceAll("(" + txtQuery.getText() + ")", "<mark>$1</mark>");
		} else {
			return target.replaceAll("(^|[\\s\\(\\)\\[\\]\\.,])(" + txtQuery.getText() + ")([\\s\\(\\)\\[\\]\\.,]|$)",
					"$1<mark>$2</mark>$3");
		}
	}
	
	private String lowlight(String target) {	
		return target.replaceAll("</?mark>", "");
	}

	private void toggleHighLighting(boolean on, List<MyDictionaryEntry> entries) {
		for (MyDictionaryEntry entry : entries) {
			if (on) {
				if(chkRev.getValue()){
					entry.setEng(highlight(entry.getEng()));
				} else {
					entry.setRus(highlight(entry.getRus()));
				}
			} else {
				if(chkRev.getValue()){
					entry.setEng(lowlight(entry.getEng()));
				} else {
					entry.setRus(lowlight(entry.getRus()));
				}
			}
		}
	}

}
