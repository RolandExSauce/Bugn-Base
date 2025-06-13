document.addEventListener("DOMContentLoaded", () => {
    const produktListe = document.getElementById("produktListe");
    const produkte = Array.from(produktListe.getElementsByClassName("produkt"));

    // Filtergruppen definieren (Checkboxen)
    const filtergruppen = {
        instrument: document.querySelectorAll('input[id^="instrument"]'),
        preis: document.querySelectorAll('input[id^="preis"]'),
        marke: document.querySelectorAll('input[id^="marke"]'),
        bewertung: document.querySelectorAll('input[id^="bewertung"]')
    };

    // Hole ausgewählte Werte einer Gruppe
    function getCheckedValues(nodelist) {
        return Array.from(nodelist)
            .filter(cb => cb.checked)
            .map(cb => cb.value);
    }

    // Filterfunktion
    function filterProdukte() {
        const aktiveFilter = {
            instrument: getCheckedValues(filtergruppen.instrument),
            preis: getCheckedValues(filtergruppen.preis),
            marke: getCheckedValues(filtergruppen.marke),
            bewertung: getCheckedValues(filtergruppen.bewertung).map(Number)
        };

        produkte.forEach(produkt => {
            const matchesInstrument = !aktiveFilter.instrument.length || aktiveFilter.instrument.includes(produkt.dataset.instrument);
            const matchesPreis = !aktiveFilter.preis.length || aktiveFilter.preis.includes(produkt.dataset.preis);
            const matchesMarke = !aktiveFilter.marke.length || aktiveFilter.marke.includes(produkt.dataset.marke);
            const produktBewertung = parseFloat(produkt.dataset.bewertung);
            const matchesBewertung =
                !aktiveFilter.bewertung.length || aktiveFilter.bewertung.some(min => produktBewertung >= min);

            const sichtbar = matchesInstrument && matchesPreis && matchesMarke && matchesBewertung;
            produkt.style.display = sichtbar ? "" : "none";
        });
    }

    // EventListener setzen
    Object.values(filtergruppen).forEach(group => {
        group.forEach(cb => cb.addEventListener("change", filterProdukte));
    });

    // Initial einmal filtern
    filterProdukte();
});
