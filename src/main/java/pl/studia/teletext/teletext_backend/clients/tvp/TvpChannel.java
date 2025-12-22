package pl.studia.teletext.teletext_backend.clients.tvp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TvpChannel {
  TVP_ABC("ABC", "TVPABC", "TVP ABC"),
  ALFA_TVP("ALFA", "ALFATVP", "ALFA TVP"),
  TVP_ABC2("DMPR", "TVPABC2", "TVP ABC 2"),
  TVP_DOKUMENT("DOK", "TVPDokument", "TVP Dokument"),
  TVP_HISTORIA2("H2", "TVPHistoria2", "TVP Historia 2"),
  TVP_INFO("INF", "TVPHistoria2", "TVP Info"),
  TVP_KOBIETA("KBT", "TVPKobieta", "TVP Kobieta"),
  TVP_HD("KHSH", "TVPHD", "TVP HD"),
  TVP_SPORT("KSP", "TVPSport", "TVP Sport"),
  TVP_KULTURA2("KUL2", "TVPKultura2", "TVP Kultura 2"),
  TVP_NAUKA("NK", "TVPNauka", "TVP Nauka"),
  TVP_WORLD("PIE", "TVPWorld", "TVP World"),
  TVP1("T1D", "TVP1", "TVP 1"),
  TVP2("T2D", "TVP2", "TVP 2"),
  TVP3("T3D", "TVP3", "TVP 3"),
  TVP_POLONIA("T4D", "TVPPolonia", "TVP Polonia"),
  TVP_KULTURA("T5D", "TVPKultura", "TVP Kultura"),
  TVP_HISTORIA("TKH", "TVPHistoria", "TVP Historia"),
  TVP_ROZRYWKA("TRO", "TVPRozrywka", "TVP Rozrywka"),
  TVP_SERIALE("TRS", "TVPSeriale", "TVP Seriale"),
  TV_BIELSAT("TVBI", "TVBielsat", "TV Bielsat"),
  TVP_WILNO("WILNO", "TVPWilno", "TVP Wilno"),

  TVP3_WARSZAWA("XAA", "TVP3Warszawa", "TVP 3 Warszawa"),
  TVP3_BYDGOSZCZ("XBB", "TVP3Bydgoszcz", "TVP 3 Bydgoszcz"),
  TVP3_BIALYSTOK("XCC", "TVP3Białystok", "TVP 3 Białystok"),
  TVP3_LODZ("XDD", "TVP3Łódź", "TVP 3 Łódź"),
  TVP3_KIELCE("XEE", "TVP3Kielce", "TVP 3 Kielce"),
  TVP3_GORZOW("XFF", "TVP3Gorzów", "TVP 3 Gorzów"),
  TVP3_GDANSK("XGG", "TVP3Gdańsk", "TVP 3 Gdańsk"),
  TVP3_OLSZTYN("XHH", "TVP3Olsztyn", "TVP 3 Olsztyn"),
  TVP3_OPOLE("XJJ", "TVP3Opole", "TVP 3 Opole"),
  TVP3_KRAKOW("XKK", "TVP3Kraków", "TVP 3 Kraków"),
  TVP3_LUBLIN("XLL", "TVP3Lublin", "TVP 3 Lublin"),
  TVP3_POZNAN("XPP", "TVP3Poznań", "TVP 3 Poznań"),
  TVP3_RZESZOW("XRR", "TVP3Rzeszów", "TVP 3 Rzeszów"),
  TVP3_SZCZECIN("XSS", "TVP3Szczecin", "TVP 3 Szczecin"),
  TVP3_KATOWICE("XTT", "TVP3Katowice", "TVP 3 Katowice"),
  TVP3_WROCLAW("XWW", "TVP3Wrocław", "TVP 3 Wrocław");

  private final String urlCode;
  private final String urlName;
  private final String displayName;
}
