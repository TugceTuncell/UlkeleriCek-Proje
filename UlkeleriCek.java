import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedWriter;


public class UlkeleriCek {
    public static void main(String[] args) {
        Scanner giris = new Scanner(System.in);
        boolean cikis = false;

        while (!cikis) {
            System.out.println("******ANA MENÜ******");
            System.out.println("1.Veri Çek");
            System.out.println("2.Listele");
            System.out.println("3.Güncelle");
            System.out.println("4.Sil");
            System.out.println("5.Çıkış");
            System.out.print("Seçim Yapınız: ");

            int secim = giris.nextInt();
            giris.nextLine(); 

            switch (secim) {
                case 1:
                    VeriCek();
                    break;
                case 2:
                    Listele();
                    break;
                case 3:
                    Güncelle();
                    break;
                case 4:
                    Sil();
                    break;
                case 5:
                    cikis=true;
                    break;
                default:
                    System.out.println("Geçersiz seçim.Lütfen tekrar deneyiniz.");
            }
        }
        System.out.println("Program sonlandırılıyor.");
    }
    
    public static void VeriCek() {
        try {
            String bolgeAdi = "Europe"; 
            String apiURL = "https://restcountries.com/v3.1/region/" + URLEncoder.encode(bolgeAdi, "UTF-8");

            URI uri = URI.create(apiURL);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            String json = result.toString();
            String[] ulkeler = json.split("\\},\\{");

            String dosyaAdi="ulkeler.txt";
            FileWriter fw = new FileWriter(dosyaAdi,true);
            fw.write("ÜlkeAdı;Başkent;Bölge;Dil;ParaBirimi;Nüfus\n");

            for(String ulke : ulkeler) {
                String ulkeAdi = parcaGetir(ulke, "\"common\":\"");
                String baskent = parcaGetir(ulke, "\"capital\":[\"");
                String bolge = parcaGetir(ulke, "\"region\":\"");

                String dil = "";
                if (ulke.contains("\"languages\":{")) {
                    String dildenSonra = ulke.split("\"languages\":\\{")[1];
                    dil = parcaGetir(dildenSonra, "\":\"");
                }

                String paraBirimi = "";
                if (ulke.contains("\"currencies\":{")) {
                    try {
                        String ParaBirimiBilgi = ulke.split("\"currencies\":\\{")[1];
                        String ParaBirimi = ParaBirimiBilgi.split(":")[0].replaceAll("\"", "").trim();
                        paraBirimi = parcaGetir(ParaBirimiBilgi, "\"name\":\"");
                    } catch (Exception e) {
                        paraBirimi = "Bilinmiyor";
                    }
                }
                String nufus = "";
                if (ulke.contains("\"population\":")) {
                   try {
                       nufus = ulke.split("\"population\":")[1].split(",")[0].trim();
                   } catch (Exception e) {
                       nufus = "0";
                   }
                }
                fw.write(ulkeAdi + ";" + baskent + ";" + bolge + ";" + dil + ";" + paraBirimi +";"+nufus +"\n");
            }

            fw.close();
            System.out.println("Veriler başarıyla çekildi ve ulkeler.txt dosyasına yazıldı.");

        } catch (Exception e) {
            System.out.println("Veri çekme sırasında hata oluştu: " + e.getMessage());  
        }
    }

    public static String parcaGetir(String veri, String anahtar) {
        int index = veri.indexOf(anahtar);
        if (index == -1) return null;
        int basla = index + anahtar.length();
        int bitis = veri.indexOf("\"", basla);
        return veri.substring(basla, bitis).replace("\\r\\n", "\n");
    }

    public static void Listele() {
        boolean cikis = false;
        Scanner giris=new Scanner(System.in);

        while (!cikis) {
            System.out.println("\n--- Listeleme Menüsü ---");
            System.out.println("a. Dili İngilizce olan ülkeleri listele");
            System.out.println("b. Euro para birimini kullanan ülkeleri listele");
            System.out.println("c. Verilerin hepsini  listele");
            System.out.println("d. Dili Almanca olan ülkeleri listele");
            System.out.println("e. Nüfusu 5 milyondan fazla olan ülkeleri listele ");
            System.out.println("f. Ana menüye dön");
            System.out.print("Seçim yapınız: ");

            char secim = giris.next().charAt(0);
            switch (secim) {
                case 'a':
                    OzellikListele("dil", "English");
                    break;
                case 'b':
                    OzellikListele("paraBirimi", "Euro");
                    break;
                case 'c':
                   OzellikListele(null,null);
                    break;
                case 'd':
                   OzellikListele("dil", "German");
                    break;
                case 'e':
                   OzellikListele("nufusBuyuk", null);
                    break;
                case 'f':
                   cikis = true;
                    break;
                default:
                   System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
            }
        }
    }

    public static void OzellikListele(String kriter,String deger) {
        try (BufferedReader br = new BufferedReader(new FileReader("ulkeler.txt"))) {
            String satir;
            boolean baslikSatiri = true;
            boolean bulunan = false;

            while ((satir = br.readLine()) != null) {
                if (baslikSatiri) {
                    baslikSatiri = false;
                    if (kriter == null) {
                        System.out.println(satir); 
                   }
                continue;
                }
             String[] parcalar = satir.split(";");
             if (parcalar.length < 6) continue;

             if (kriter == null) {
                 System.out.println(satir); 
                 bulunan = true;
             }else if (kriter.equals("nufusBuyuk")) {
                try {
                    long nufus = Long.parseLong(parcalar[5].trim());
                    if (nufus > 5_000_000) {
                        System.out.println(parcalar[0]);
                        bulunan = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Hatalı nüfus verisi: " + parcalar[0]);
                }
            } else {
                String degerIndex = "";
                switch (kriter) {
                    case "dil": degerIndex = parcalar[3];
                    break;
                    case "paraBirimi": degerIndex = parcalar[4]; 
                    break;
                }
                if (degerIndex.equalsIgnoreCase(deger)) {
                    System.out.println(parcalar[0]); 
                    bulunan = true;
                }
            }
        }
        if (!bulunan)
            System.out.println("Girdiğiniz bilgiye uygun kayıt bulunamadı.");
        }catch (IOException e) {
        System.out.println("Dosya okuma hatası oluştu: " + e.getMessage());
      }
    }

    public static void Güncelle() {
        Scanner giris=new Scanner(System.in);
        System.out.print("Güncellemek istediğiniz ülke adını yazınız: ");
        String araAdi = giris.nextLine();

        List<String> satirlar = new ArrayList<>();
        boolean bulundu = false;

        try (BufferedReader br = new BufferedReader(new FileReader("ulkeler.txt"))) {
            String baslik = br.readLine(); 
            satirlar.add(baslik);

            String satir;
            while ((satir = br.readLine()) != null) {
                String[] parcalar = satir.split(";");
                if (parcalar.length < 6) {
                    satirlar.add(satir);
                    continue;
                }
                if (parcalar[0].equalsIgnoreCase(araAdi)) {
                    System.out.println("Bulunan kayıt: " + satir);
                    System.out.print("Değiştirmek istediğiniz veri bu mu ? (e/h): ");
                    String cevap = giris.nextLine();

                    if (cevap.equalsIgnoreCase("e")) {
                        String yeniAdi = parcalar[0];
                        String yeniBaskent = parcalar[1];
                        String yeniBolge = parcalar[2];
                        String yeniDil = parcalar[3];
                        String yeniPara = parcalar[4];
                        String yeniNufus=parcalar[5];

                        boolean guncelleDevam = true;
                        while (guncelleDevam) {
                            System.out.println("\n***GÜNCELLEME MENÜSÜ***");
                            System.out.println("Hangi veriyi güncellemek istiyorsunuz?");
                            System.out.println("1. Ülke Adı");
                            System.out.println("2. Başkent");
                            System.out.println("3. Bölge");
                            System.out.println("4. Dil");
                            System.out.println("5. Para Birimi");
                            System.out.println("6. Nüfus");
                            System.out.println("7. Güncelleme işlemini bitir.");
                            System.out.println("8. Tüm alanları güncelle");
                            System.out.print("Seçiminiz: ");

                            String secim = giris.nextLine();

                            switch (secim) {
                                case "1":
                                    System.out.print("Yeni Ülke Adı: ");
                                    yeniAdi = giris.nextLine();
                                    break;
                                case "2":
                                    System.out.print("Yeni Başkent: ");
                                    yeniBaskent = giris.nextLine();
                                    break;
                                case "3":
                                    System.out.print("Yeni Bölge: ");
                                    yeniBolge = giris.nextLine();
                                    break;
                                case "4":
                                    System.out.print("Yeni Dil: ");
                                    yeniDil = giris.nextLine();
                                    break;
                                case "5":
                                    System.out.print("Yeni Para Birimi: ");
                                    yeniPara = giris.nextLine();
                                    break;
                                case "6":
                                    System.out.println("Yeni Nüfus:");
                                    yeniNufus=giris.nextLine();
                                    break;
                                case "7":
                                    guncelleDevam = false;
                                    break;
                                case "8":
                                    System.out.print("Yeni Ülke Adı: ");
                                    yeniAdi = giris.nextLine();
                                    System.out.print("Yeni Başkent: ");
                                    yeniBaskent = giris.nextLine();
                                    System.out.print("Yeni Bölge: ");
                                    yeniBolge = giris.nextLine();
                                    System.out.print("Yeni Dil: ");
                                    yeniDil = giris.nextLine();
                                    System.out.print("Yeni Para Birimi: ");
                                    yeniPara = giris.nextLine();
                                    System.out.print("Yeni Nüfus: ");
                                    yeniNufus = giris.nextLine();
                                    guncelleDevam = false;
                                    break;
                                default:
                                    System.out.println("Geçersiz seçim, tekrar deneyin.");
                        }
                    }

                    String yeniSatir = yeniAdi + ";" + yeniBaskent + ";" + yeniBolge + ";" + yeniDil + ";" + yeniPara + ";" +yeniNufus;
                    satirlar.add(yeniSatir);
                    bulundu = true;
                } else {
                    satirlar.add(satir);
                }
            } else {
                satirlar.add(satir);
            }               
        }

        if (!bulundu) {
            System.out.println("Aradığınız ülke bulunamadı, güncelleme yapılmadı.");
        } else {
            try (FileWriter fw = new FileWriter("ulkeler.txt")) {
                for (String s : satirlar) {
                    fw.write(s + "\n");
                }
            }
            System.out.println("Dosya başarıyla güncellendi.");
        }
        } catch (IOException e) {
            System.out.println("Dosya güncelleme hatası oluştu: " + e.getMessage());
      }
    }   
    public static void Sil(){
      Scanner giris=new Scanner(System.in);
      System.out.print("Silmek istediğiniz ülke adını yazınız: ");
      String araAdi = giris.nextLine();

      List<String> satirlar = new ArrayList<>();
      boolean bulundu = false;

        try (BufferedReader br = new BufferedReader(new FileReader("ulkeler.txt"))) {
           String baslik = br.readLine(); 
           satirlar.add(baslik);

           String satir;
            while ((satir = br.readLine()) != null) {
               String[] parcalar = satir.split(";");
                if (parcalar.length < 6) {
                   satirlar.add(satir);
                   continue;
                }
                if (parcalar[0].equalsIgnoreCase(araAdi)) {
                System.out.println("Bulunan kayıt: " + satir);
                System.out.print("Değiştirmek istediğiniz veri bu mu ? (e/h): ");
                String cevap = giris.nextLine();

                  if (cevap.equalsIgnoreCase("e")) {
                     String yeniAdi = parcalar[0];
                     String yeniBaskent = parcalar[1];
                     String yeniBolge = parcalar[2];
                     String yeniDil = parcalar[3];
                     String yeniPara = parcalar[4];
                     String yeniNüfus = parcalar[5];

                     boolean SilDevam = true;
                     while (SilDevam) {
                    System.out.println("\n***SİLME MENÜSÜ***");
                    System.out.println("Hangi veriyi silmek istiyorsunuz?");
                    System.out.println("1. Ülke Adı");
                    System.out.println("2. Başkent");
                    System.out.println("3. Bölge");
                    System.out.println("4. Dil");
                    System.out.println("5. Para Birimi");
                    System.out.println("6. Nüfus");
                    System.out.println("7. Silme işlemini bitir.");
                    System.out.print("Seçiminiz: ");

                    String secim = giris.nextLine();

                switch (secim) {
                   case "1":
                       parcalar[0]="";
                    break;
                   case "2":
                       parcalar[1]="";
                    break;
                   case "3":
                       parcalar[2]="";
                    break;
                   case "4":
                       parcalar[3]="";
                    break;
                   case "5":
                       parcalar[4]="";
                    break;
                   case "6":
                       parcalar[5]="";
                    break;
                   case "7":
                       SilDevam=false;
                    break;   
                   default:
                       System.out.println("Geçersiz seçim, tekrar deneyin.");
                }
            }

             String yeniSatir = String.join(";",parcalar);
                satirlar.add(yeniSatir);
                bulundu = true;
            } else {
                satirlar.add(satir);
            }
            } else {
                satirlar.add(satir);
           }
        }
         if (bulundu) {
            try (BufferedWriter bw = Files.newBufferedWriter(Path.of("ulkeler.txt"), StandardCharsets.UTF_8)) {
                for (String s : satirlar) {
                    bw.write(s);
                    bw.newLine();
                }
                System.out.println("Silme işlemi tamamlandı ve dosya güncellendi.");
            }catch(IOException e) {
                    System.out.println("Dosya yazma hatası: " + e.getMessage());
            } 
           }else {
               System.out.println("Ülke bulunamadı, silme işlemi yapılmadı.");
           }

        } catch (IOException e) {
               System.out.println("Dosya silme hatası oluştu: " + e.getMessage());
       }
    }
}