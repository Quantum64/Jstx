package co.q64.jstx.lang.opcode;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import co.q64.jstx.compression.lzma.LZMAByteArrayCompressorFactory;
import co.q64.jstx.compression.lzma.LZMAByteArrayDecompressorFactory;
import co.q64.jstx.compression.lzma.UTF8;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Chars {
	// @formatter:off
	x00(0x00, "ø"),
	x01(0x01, "☺"),
	x02(0x02, "☻"),
	x03(0x03, "♥"),
	x04(0x04, "♦"),
	x05(0x05, "♣"),
	x06(0x06, "♠"),
	x07(0x07, "•"),
	x08(0x08, "◘"),
	x09(0x09, "○"),
	x0a(0x0a, "◙"),
	x0b(0x0b, "♂"),
	x0c(0x0c, "♀"),
	x0d(0x0d, "♪"),
	x0e(0x0e, "♫"),
	x0f(0x0f, "☼"),
	x10(0x10, "►"),
	x11(0x11, "◄"),
	x12(0x12, "↕"),
	x13(0x13, "‼"),
	x14(0x14, "¶"),
	x15(0x15, "§"),
	x16(0x16, "▬"),
	x17(0x17, "↨"),
	x18(0x18, "↑"),
	x19(0x19, "↓"),
	x1a(0x1a, "→"),
	x1b(0x1b, "←"),
	x1c(0x1c, "∟"),
	x1d(0x1d, "↔"),
	x1e(0x1e, "▲"),
	x1f(0x1f, "▼"),
	x20(0x20, "☯"),
	x21(0x21, "!"),
	x22(0x22, "\""),
	x23(0x23, "#"),
	x24(0x24, "$"),
	x25(0x25, "%"),
	x26(0x26, "&"),
	x27(0x27, "'"),
	x28(0x28, "("),
	x29(0x29, ")"),
	x2a(0x2a, "*"),
	x2b(0x2b, "+"),
	x2c(0x2c, ","),
	x2d(0x2d, "-"),
	x2e(0x2e, "."),
	x2f(0x2f, "/"),
	x30(0x30, "0"),
	x31(0x31, "1"),
	x32(0x32, "2"),
	x33(0x33, "3"),
	x34(0x34, "4"),
	x35(0x35, "5"),
	x36(0x36, "6"),
	x37(0x37, "7"),
	x38(0x38, "8"),
	x39(0x39, "9"),
	x3a(0x3a, ":"),
	x3b(0x3b, ";"),
	x3c(0x3c, "<"),
	x3d(0x3d, "="),
	x3e(0x3e, ">"),
	x3f(0x3f, "?"),
	x40(0x40, "@"),
	x41(0x41, "A"),
	x42(0x42, "B"),
	x43(0x43, "C"),
	x44(0x44, "D"),
	x45(0x45, "E"),
	x46(0x46, "F"),
	x47(0x47, "G"),
	x48(0x48, "H"),
	x49(0x49, "I"),
	x4a(0x4a, "J"),
	x4b(0x4b, "K"),
	x4c(0x4c, "L"),
	x4d(0x4d, "M"),
	x4e(0x4e, "N"),
	x4f(0x4f, "O"),
	x50(0x50, "P"),
	x51(0x51, "Q"),
	x52(0x52, "R"),
	x53(0x53, "S"),
	x54(0x54, "T"),
	x55(0x55, "U"),
	x56(0x56, "V"),
	x57(0x57, "W"),
	x58(0x58, "X"),
	x59(0x59, "Y"),
	x5a(0x5a, "Z"),
	x5b(0x5b, "["),
	x5c(0x5c, "\\"),
	x5d(0x5d, "]"),
	x5e(0x5e, "^"),
	x5f(0x5f, "_"),
	x60(0x60, "`"),
	x61(0x61, "a"),
	x62(0x62, "b"),
	x63(0x63, "c"),
	x64(0x64, "d"),
	x65(0x65, "e"),
	x66(0x66, "f"),
	x67(0x67, "g"),
	x68(0x68, "h"),
	x69(0x69, "i"),
	x6a(0x6a, "j"),
	x6b(0x6b, "k"),
	x6c(0x6c, "l"),
	x6d(0x6d, "m"),
	x6e(0x6e, "n"),
	x6f(0x6f, "o"),
	x70(0x70, "p"),
	x71(0x71, "q"),
	x72(0x72, "r"),
	x73(0x73, "s"),
	x74(0x74, "t"),
	x75(0x75, "u"),
	x76(0x76, "v"),
	x77(0x77, "w"),
	x78(0x78, "x"),
	x79(0x79, "y"),
	x7a(0x7a, "z"),
	x7b(0x7b, "{"),
	x7c(0x7c, "|"),
	x7d(0x7d, "}"),
	x7e(0x7e, "~"),
	x7f(0x7f, "⌂"),
	x80(0x80, "Ç"),
	x81(0x81, "ü"),
	x82(0x82, "é"),
	x83(0x83, "â"),
	x84(0x84, "ä"),
	x85(0x85, "à"),
	x86(0x86, "å"),
	x87(0x87, "ç"),
	x88(0x88, "ê"),
	x89(0x89, "ë"),
	x8a(0x8a, "è"),
	x8b(0x8b, "ï"),
	x8c(0x8c, "î"),
	x8d(0x8d, "ì"),
	x8e(0x8e, "Ä"),
	x8f(0x8f, "Å"),
	x90(0x90, "É"),
	x91(0x91, "æ"),
	x92(0x92, "Æ"),
	x93(0x93, "ô"),
	x94(0x94, "ö"),
	x95(0x95, "ò"),
	x96(0x96, "û"),
	x97(0x97, "ù"),
	x98(0x98, "ÿ"),
	x99(0x99, "Ö"),
	x9a(0x9a, "Ü"),
	x9b(0x9b, "¢"),
	x9c(0x9c, "£"),
	x9d(0x9d, "¥"),
	x9e(0x9e, "₧"),
	x9f(0x9f, "ƒ"),
	xa0(0xa0, "á"),
	xa1(0xa1, "í"),
	xa2(0xa2, "ó"),
	xa3(0xa3, "ú"),
	xa4(0xa4, "ñ"),
	xa5(0xa5, "Ñ"),
	xa6(0xa6, "ª"),
	xa7(0xa7, "º"),
	xa8(0xa8, "¿"),
	xa9(0xa9, "⌐"),
	xaa(0xaa, "¬"),
	xab(0xab, "½"),
	xac(0xac, "¼"),
	xad(0xad, "¡"),
	xae(0xae, "«"),
	xaf(0xaf, "»"),	
	xb0(0xb0, "░"),
	xb1(0xb1, "▒"),
	xb2(0xb2, "▓"),
	xb3(0xb3, "│"),
	xb4(0xb4, "┤"),
	xb5(0xb5, "╡"),
	xb6(0xb6, "╢"),
	xb7(0xb7, "╖"),
	xb8(0xb8, "╕"),
	xb9(0xb9, "╣"),
	xba(0xba, "║"),
	xbb(0xbb, "╗"),
	xbc(0xbc, "╝"),
	xbd(0xbd, "╜"),
	xbe(0xbe, "╛"),
	xbf(0xbf, "┐"),
	xc0(0xc0, "└"),
	xc1(0xc1, "┴"),
	xc2(0xc2, "┬"),
	xc3(0xc3, "├"),
	xc4(0xc4, "─"),
	xc5(0xc5, "┼"),
	xc6(0xc6, "╞"),
	xc7(0xc7, "╟"),
	xc8(0xc8, "╚"),
	xc9(0xc9, "╔"),
	xca(0xca, "╩"),
	xcb(0xcb, "╦"),
	xcc(0xcc, "╠"),
	xcd(0xcd, "═"),
	xce(0xce, "╬"),
	xcf(0xcf, "╧"),
	xd0(0xd0, "╨"),
	xd1(0xd1, "╤"),
	xd2(0xd2, "╥"),
	xd3(0xd3, "╙"),
	xd4(0xd4, "╘"),
	xd5(0xd5, "╒"),
	xd6(0xd6, "╓"),
	xd7(0xd7, "╫"),
	xd8(0xd8, "╪"),
	xd9(0xd9, "┘"),
	xda(0xda, "┌"),
	xdb(0xdb, "█"),
	xdc(0xdc, "▄"),
	xdd(0xdd, "▌"),
	xde(0xde, "▐"),
	xdf(0xdf, "▀"),
	xe0(0xe0, "α"),
	xe1(0xe1, "ß"),
	xe2(0xe2, "Γ"),
	xe3(0xe3, "π"),
	xe4(0xe4, "Σ"),
	xe5(0xe5, "σ"),
	xe6(0xe6, "µ"),
	xe7(0xe7, "τ"),
	xe8(0xe8, "Φ"),
	xe9(0xe9, "Θ"),
	xea(0xea, "Ω"),
	xeb(0xeb, "δ"),
	xec(0xec, "∞"),
	xed(0xed, "φ"),
	xee(0xee, "ε"),
	xef(0xef, "∩"),
	xf0(0xf0, "≡"),
	xf1(0xf1, "±"),
	xf2(0xf2, "≥"),
	xf3(0xf3, "≤"),
	xf4(0xf4, "⌠"),
	xf5(0xf5, "⌡"),
	xf6(0xf6, "÷"),
	xf7(0xf7, "≈"),
	xf8(0xf8, "°"),
	xf9(0xf9, "∙"),
	xfa(0xfa, "·"),
	xfb(0xfb, "√"),
	xfc(0xfc, "ⁿ"),
	xfd(0xfd, "²"),
	xfe(0xfe, "■"),
	xff(0xff, "Δ");
	// @formatter:on
	private int id;
	private String character;

	private static final Chars[] chars = Chars.values();

	public static Chars fromCode(String code) {
		for (Chars c : chars) {
			if (c.getCharacter().equals(code)) {
				return c;
			}
		}
		return Chars.x00;
	}

	public static Chars fromByte(byte value) {
		return fromInt(value + 128);
	}

	public static Chars fromInt(int value) {
		for (Chars c : chars) {
			if (c.getId() == value) {
				return c;
			}
		}
		return Chars.x00;
	}

	public byte getByte() {
		return (byte) (id - 128);
	}

	public static void main(String[] args) {
		System.out.println("☯".length());
		String str = "+----------+----------+----------+\\n|a.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|b.exe 10% |#         |leeching  |\\n+----------+----------+----------+\\n|c.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|d.exe 20% |##        |leeching  |\\n+----------+----------+----------+\\n|e.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|f.exe 30% |###       |leeching  |\\n+----------+----------+----------+\\n|g.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|h.exe 40% |####      |leeching  |\\n+----------+----------+----------+\\n|i.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|j.exe 50% |#####     |leeching  |\\n+----------+----------+----------+\\n|k.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|l.exe 60% |######    |leeching  |\\n+----------+----------+----------+\\n|m.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|n.exe 70% |#######   |leeching  |\\n+----------+----------+----------+\\n|o.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|p.exe 80% |########  |leeching  |\\n+----------+----------+----------+\\n|q.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|r.exe 90% |######### |leeching  |\\n+----------+----------+----------+\\n|s.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|t.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|u.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|v.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|w.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|x.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|y.exe     |##########|seeding   |\\n+----------+----------+----------+\\n|z.exe     |##########|seeding   |\\n+----------+----------+----------+";

		StringBuilder comp = new StringBuilder();
		byte[] compressed = new LZMAByteArrayCompressorFactory().create(UTF8.encode(str)).getCompressedData();
		for (byte b : compressed) {
			comp.append(fromByte(b).getCharacter());
		}
		System.out.println(comp);

		byte[] data = new byte[comp.length()];
		char[] chars = comp.toString().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			data[i] = fromCode(String.valueOf(chars[i])).getByte();
		}
		byte[] decoded = new LZMAByteArrayDecompressorFactory().create(data).getUncompressedData();
		System.out.println(IntStream.range(0, compressed.length).mapToObj(i -> compressed[i]).map(Object::toString).collect(Collectors.joining(",")));
		System.out.println(IntStream.range(0, data.length).mapToObj(i -> data[i]).map(Object::toString).collect(Collectors.joining(",")));
		System.out.println(UTF8.decode(decoded));
		//System.out.println(UTF8.decode(new LZMAByteArrayDecompressorFactory().create(new LZMAByteArrayCompressorFactory().create(UTF8.encode(str)).getCompressedData()).getUncompressedData()));
	}
}
