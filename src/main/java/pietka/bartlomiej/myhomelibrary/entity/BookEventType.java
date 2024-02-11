package pietka.bartlomiej.myhomelibrary.entity;

public enum BookEventType {

    DODANIE_KSIAZKI("D", "Książka została dodana"),
    EDYCJA_KSIAZKI("E", "Książka została zmodyfikowana"),
    DODANIE_OKLADKI("DO", "Została dodana okładka"),
    POZYCZENIE_KSIAZKI("P", "Książka została pożyczona"),
    ZWROCENIE_KSIAZKI("Z", "Książka została zwrócona"),
    ZAGUBIENIE_KSIAZKI("ZK", "Książka została zgubiona"),
    ODNALEZIENIE_KSIAZKI("OK", "Książka została odnaleziona"),
    USZKODZENIE_KSIAZKI("UK", "Książka została uszkodzona"),
    ZNISZCZENIE_KSIAZKI("ZNK", "Książka została zniszczona");

    private final String key;
    private final String value;

    BookEventType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
