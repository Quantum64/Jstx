package co.q64.jstx.opcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Chars {
	// TODO
	// @formatter:off
	x00(0x00, "ø"),
	x01(0x00, "☺"),
	x02(0x00, "☻"),
	x03(0x00, "♥"),
	x04(0x00, "♦"),
	x05(0x00, "♣"),
	x06(0x00, "♠"),
	x07(0x00, "•"),
	x08(0x00, "◘"),
	x09(0x00, "○"),
	x0a(0x00, "◙"),
	x0b(0x00, "♂"),
	x0c(0x00, "♀"),
	x0d(0x00, "♪"),
	x0e(0x00, "♫"),
	x0f(0x00, "☼"),
	x10(0x00, "►"),
	x11(0x00, "◄"),
	x12(0x00, "↕"),
	x13(0x00, "‼"),
	x14(0x00, "¶"),
	x15(0x00, "§"),
	x16(0x00, "▬"),
	x17(0x00, "↨"),
	x18(0x00, "↑"),
	x19(0x00, "↓"),
	x1a(0x00, "→"),
	x1b(0x00, "←"),
	x1c(0x00, "∟"),
	x1d(0x00, "↔"),
	x1e(0x00, "▲"),
	x1f(0x00, "▼"),
	x20(0x00, " "),
	x21(0x00, "!"),
	x22(0x00, "\""),
	x23(0x00, "#"),
	x24(0x00, "$"),
	x25(0x00, "%"),
	x26(0x00, "&"),
	x27(0x00, "'"),
	x28(0x00, "("),
	x29(0x00, ")"),
	x2a(0x00, "*"),
	x2b(0x00, "+"),
	x2c(0x00, ","),
	x2d(0x00, "-"),
	x2e(0x00, "."),
	x2f(0x00, "/"),
	x30(0x00, "0"),
	x31(0x00, "1"),
	x32(0x00, "2"),
	x33(0x00, "3"),
	x34(0x00, "4"),
	x35(0x00, "5"),
	x36(0x00, "6"),
	x37(0x00, "7"),
	x38(0x00, "8"),
	x39(0x00, "9"),
	x3a(0x00, ":"),
	x3b(0x00, ";"),
	x3c(0x00, "<"),
	x3d(0x00, "="),
	x3e(0x00, ">"),
	x3f(0x00, "?"),
	x40(0x00, ""),
	x41(0x00, ""),
	x42(0x00, ""),
	x43(0x00, ""),
	x44(0x00, ""),
	x45(0x00, ""),
	x46(0x00, ""),
	x47(0x00, ""),
	x48(0x00, ""),
	x49(0x00, ""),
	x4a(0x00, ""),
	x4b(0x00, ""),
	x4c(0x00, ""),
	x4d(0x00, ""),
	x4e(0x00, ""),
	x4f(0x00, ""),
	x50(0x00, ""),
	x51(0x00, ""),
	x52(0x00, ""),
	x53(0x00, ""),
	x54(0x00, ""),
	x55(0x00, ""),
	x56(0x00, ""),
	x57(0x00, ""),
	x58(0x00, ""),
	x59(0x00, ""),
	x5a(0x00, ""),
	x5b(0x00, ""),
	x5c(0x00, ""),
	x5d(0x00, ""),
	x5e(0x00, ""),
	x5f(0x00, ""),
	x60(0x00, ""),
	x61(0x00, ""),
	x62(0x00, ""),
	x63(0x00, ""),
	x64(0x00, ""),
	x65(0x00, ""),
	x66(0x00, ""),
	x67(0x00, ""),
	x68(0x00, ""),
	x69(0x00, ""),
	x6a(0x00, ""),
	x6b(0x00, ""),
	x6c(0x00, ""),
	x6d(0x00, ""),
	x6e(0x00, ""),
	x6f(0x00, ""),
	x70(0x00, ""),
	x71(0x00, ""),
	x72(0x00, ""),
	x73(0x00, ""),
	x74(0x00, ""),
	x75(0x00, ""),
	x76(0x00, ""),
	x77(0x00, ""),
	x78(0x00, ""),
	x79(0x00, ""),
	x7a(0x00, ""),
	x7b(0x00, ""),
	x7c(0x00, ""),
	x7d(0x00, ""),
	x7e(0x00, ""),
	x7f(0x00, ""),
	x80(0x00, ""),
	x81(0x00, ""),
	x82(0x00, ""),
	x83(0x00, ""),
	x84(0x00, ""),
	x85(0x00, ""),
	x86(0x00, ""),
	x87(0x00, ""),
	x88(0x00, ""),
	x89(0x00, ""),
	x8a(0x00, ""),
	x8b(0x00, ""),
	x8c(0x00, ""),
	x8d(0x00, ""),
	x8e(0x00, ""),
	x8f(0x00, ""),
	x90(0x00, ""),
	x91(0x00, ""),
	x92(0x00, ""),
	x93(0x00, ""),
	x94(0x00, ""),
	x95(0x00, ""),
	x96(0x00, ""),
	x97(0x00, ""),
	x98(0x00, ""),
	x99(0x00, ""),
	x9a(0x00, ""),
	x9b(0x00, ""),
	x9c(0x00, ""),
	x9d(0x00, ""),
	x9e(0x00, ""),
	x9f(0x00, ""),
	xa0(0x00, ""),
	xa1(0x00, ""),
	xa2(0x00, ""),
	xa3(0x00, ""),
	xa4(0x00, ""),
	xa5(0x00, ""),
	xa6(0x00, ""),
	xa7(0x00, ""),
	xa8(0x00, ""),
	xa9(0x00, ""),
	xaa(0x00, ""),
	xab(0x00, ""),
	xac(0x00, ""),
	xad(0x00, ""),
	xae(0x00, ""),
	xaf(0x00, ""),
	xb0(0x00, ""),
	xb1(0x00, ""),
	xb2(0x00, ""),
	xb3(0x00, ""),
	xb4(0x00, ""),
	xb5(0x00, ""),
	xb6(0x00, ""),
	xb7(0x00, ""),
	xb8(0x00, ""),
	xb9(0x00, ""),
	xba(0x00, ""),
	xbb(0x00, ""),
	xbc(0x00, ""),
	xbd(0x00, ""),
	xbe(0x00, ""),
	xbf(0x00, ""),
	xc0(0x00, ""),
	xc1(0x00, ""),
	xc2(0x00, ""),
	xc3(0x00, ""),
	xc4(0x00, ""),
	xc5(0x00, ""),
	xc6(0x00, ""),
	xc7(0x00, ""),
	xc8(0x00, ""),
	xc9(0x00, ""),
	xca(0x00, ""),
	xcb(0x00, ""),
	xcc(0x00, ""),
	xcd(0x00, ""),
	xce(0x00, ""),
	xcf(0x00, ""),
	xd0(0x00, ""),
	xd1(0x00, ""),
	xd2(0x00, ""),
	xd3(0x00, ""),
	xd4(0x00, ""),
	xd5(0x00, ""),
	xd6(0x00, ""),
	xd7(0x00, ""),
	xd8(0x00, ""),
	xd9(0x00, ""),
	xda(0x00, ""),
	xdb(0x00, ""),
	xdc(0x00, ""),
	xdd(0x00, ""),
	xde(0x00, ""),
	xdf(0x00, ""),
	xe0(0x00, ""),
	xe1(0x00, ""),
	xe2(0x00, ""),
	xe3(0x00, ""),
	xe4(0x00, ""),
	xe5(0x00, ""),
	xe6(0x00, ""),
	xe7(0x00, ""),
	xe8(0x00, ""),
	xe9(0x00, ""),
	xea(0x00, ""),
	xeb(0x00, ""),
	xec(0x00, ""),
	xed(0x00, ""),
	xee(0x00, ""),
	xef(0x00, ""),
	xf0(0x00, ""),
	xf1(0x00, ""),
	xf2(0x00, ""),
	xf3(0x00, ""),
	xf4(0x00, ""),
	xf5(0x00, ""),
	xf6(0x00, ""),
	xf7(0x00, ""),
	xf8(0x00, ""),
	xf9(0x00, ""),
	xfa(0x00, ""),
	xfb(0x00, ""),
	xfc(0x00, ""),
	xfd(0x00, ""),
	xfe(0x00, ""),
	xff(0x00, "Δ"),
	NULL(0x00, "");
	// @formatter:on
	private int id;
	private String character;

	private static final Chars[] chars = Chars.values();
	public static final Chars literalBegin = x0b;
	public static final Chars literalUncompressed = x0c;
	public static final Chars literalCompressionMode1 = x0d;
	public static final Chars literalCompressionMode2 = x0e;
	public static final Chars literalCompressionMode3 = x0f;
	public static final Chars conditionalEnd = xff;
	public static final Chars literalPair = x10;
	public static final Chars literalSingle = x11;
	public static final Chars ifEqual = x12;
	public static final Chars ifNotEqual = x13;
	public static final Chars ifGreater = x14;
	public static final Chars ifGreaterOrEqual = x15;
	public static final Chars ifLess = x16;
	public static final Chars ifLessOrEqual = x17;
	public static final Chars ifElse = x18;

	public static Chars fromCode(String code) {
		for (Chars c : chars) {
			if (c.getCharacter().equals(code)) {
				return c;
			}
		}
		return Chars.x00;
	}
}
