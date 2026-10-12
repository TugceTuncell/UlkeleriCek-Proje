# Avrupa Ülkeleri Veri Yönetimi (UlkeleriCek)

Bu proje, Java ile geliştirilmiş konsol tabanlı bir REST API ve Dosya Yönetimi uygulamasıdır. `restcountries.com` API'sini kullanarak Avrupa bölgesindeki ülkelerin verilerini çeker, yerel bir metin dosyasına kaydeder ve kullanıcıya bu veriler üzerinde okuma, filtreleme, güncelleme ve temizleme işlemleri yapma imkanı sunar.

## 🚀 Özellikler

- **🌐 Veri Çekme:** REST API üzerinden Avrupa ülkeleri verilerini (Ülke Adı, Başkent, Bölge, Dil, Para Birimi, Nüfus) çeker ve `ulkeler.txt` dosyasına CSV formatında (noktalı virgül ile ayrılarak) kaydeder.

- **🔍 Gelişmiş Listeleme:** Kaydedilen veriler üzerinde şu filtrelemeleri yapabilirsiniz:
  - Dili İngilizce veya Almanca olan ülkeler.
  - Para birimi "Euro" olan ülkeler.
  - Nüfusu 5.000.000'dan büyük olan ülkeler.
  - Tüm ülkelerin listesi.
- **✏️ Güncelleme:** Metin dosyasına kaydedilmiş bir ülkenin başkent, bölge, dil, para birimi veya nüfus bilgilerini satır satır okuyarak günceller.
- **🗑️ Silme (Veri Temizleme):** Seçilen bir ülkeye ait spesifik bir veri alanını (örneğin sadece nüfus bilgisini veya başkenti) dosyadan temizler.

## 🛠️ Kullanılan Teknolojiler

- **Java (JDK 11 veya üzeri önerilir):** Proje tamamen standart Java kütüphaneleri ile yazılmıştır.
- **HttpURLConnection:** Dış API ile HTTP GET istekleri yapmak için kullanıldı.
- **File I/O (FileReader, FileWriter, NIO):** Verilerin yerel diske yazılması ve okunması için kullanıldı.
- **JSON Parsing:** Herhangi bir dış kütüphane (Gson, Jackson vb.) kullanılmadan, String manipülasyonu ile temel düzeyde JSON ayrıştırma işlemi yapıldı.

## ⚙️ Kurulum ve Çalıştırma

Projeyi bilgisayarınızda çalıştırmak için ekstra bir kütüphane kurmanıza gerek yoktur.

1. Projeyi bilgisayarınıza klonlayın:
   ```bash
   git clone [https://github.com/kullaniciadi/UlkeleriCek.git](https://github.com/kullaniciadi/UlkeleriCek.git)
