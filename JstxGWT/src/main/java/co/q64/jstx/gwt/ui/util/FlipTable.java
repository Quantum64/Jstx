package co.q64.jstx.gwt.ui.util;

public final class FlipTable {
	private static final String EMPTY = "(empty)";

	private final String[] headers;
	private final String[][] data;
	private final int columns;
	private final int[] columnWidths;
	private final int emptyWidth;

	private FlipTable(String[] headers, String[][] data) {
		this.headers = headers;
		this.data = data;

		columns = headers.length;
		columnWidths = new int[columns];
		for (int row = -1; row < data.length; row++) {
			String[] rowData = (row == -1) ? headers : data[row];
			if (rowData.length != columns) {
				throw new IllegalArgumentException();
			}
			for (int column = 0; column < columns; column++) {
				for (String rowDataLine : rowData[column].split("\\n")) {
					columnWidths[column] = Math.max(columnWidths[column], rowDataLine.length());
				}
			}
		}

		int emptyWidth = 3 * (columns - 1);
		for (int columnWidth : columnWidths) {
			emptyWidth += columnWidth;
		}
		this.emptyWidth = emptyWidth;

		if (emptyWidth < EMPTY.length()) {
			columnWidths[columns - 1] += EMPTY.length() - emptyWidth;
		}
	}

	public static String of(String[] headers, String[][] data) {
		if (headers == null)
			throw new NullPointerException("headers == null");
		if (headers.length == 0)
			throw new IllegalArgumentException("Headers must not be empty.");
		if (data == null)
			throw new NullPointerException("data == null");
		return new FlipTable(headers, data).toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		printDivider(builder, "╔═╤═╗");
		printData(builder, headers);
		if (data.length == 0) {
			printDivider(builder, "╠═╧═╣");
			builder.append('║').append(pad(emptyWidth, EMPTY)).append("║\n");
			printDivider(builder, "╚═══╝");
		} else {
			for (int row = 0; row < data.length; row++) {
				printDivider(builder, row == 0 ? "╠═╪═╣" : "╟─┼─╢");
				printData(builder, data[row]);
			}
			printDivider(builder, "╚═╧═╝");
		}
		return builder.toString();
	}

	private void printDivider(StringBuilder out, String format) {
		for (int column = 0; column < columns; column++) {
			out.append(column == 0 ? format.charAt(0) : format.charAt(2));
			out.append(pad(columnWidths[column], "").replace(' ', format.charAt(1)));
		}
		out.append(format.charAt(4)).append('\n');
	}

	private void printData(StringBuilder out, String[] data) {
		for (int line = 0, lines = 1; line < lines; line++) {
			for (int column = 0; column < columns; column++) {
				out.append(column == 0 ? '║' : '│');
				String[] cellLines = data[column].split("\\n");
				lines = Math.max(lines, cellLines.length);
				String cellLine = line < cellLines.length ? cellLines[line] : "";
				out.append(pad(columnWidths[column], cellLine));
			}
			out.append("║\n");
		}
	}

	private static String pad(int width, String data) {
		StringBuilder sb = new StringBuilder();
		sb.append(data);
		for (int i = 0; i < width - data.length(); i++) {
			sb.append(" ");
		}
		return sb.toString();
		//return String.format(" %1$-" + width + "s ", data);
	}
}
