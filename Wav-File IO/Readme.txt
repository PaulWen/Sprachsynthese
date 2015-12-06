TODOs:

Mustererkennung: 
	1. herausfinden, warum die Mustersuche ab eine Länge von 3500 solange dauert und wie man dies beschleunigen kann!
		a) Sucht man nach großen Mustern, so sucht man auch immer nach kleinen Mustern, da die großen Muster aus den kleinen bestehen!
		   Dies kann man sich auch zur Nutze machen und bei einem einmaligen durchsuchen bereits alle möglichen Muster herausfinden, indem man immer gut, vie lang am Anfang die Quote über 90 % ist, wenn ein Muster gesucht wird.
		   Falls die 90 % Quote länger oder gleich als die MinAnzahl an Frames ist, so wurde ein Muster gefunden (muss dann aber noch bezüglich seiner Häufigkeit überprüft werden!)
		   Nach derHäufigkeit überprüfen ist aber leicht, indem man einfach die ganze zeit eine Liste führt, welche auflistet bis zu wie viele Frames über der 90 % Quote waren - anschließend muss man nur die Häufigkeiten berechnen
		b) nur von jedem zweiten Sample ausgehend das Muster suchen (ggf. nicht machen, da dann das Filtern doppelt gefundener Muster nicht mehr funktioniert!)
	2. die Berechnung der Linien-Polygone kommentieren und dokumentieren!
	3. die Polygon-Größe für die Höhe und Breite getrennt definieren können
	4. neue Muster automatisiert finden (= jede mögliche Kombination von Samples als Muster versuchen - wenn es öfter als 3 mal gefunden wird, gilt eine Kombination als Muster)
	
Verbesserungsvorschläge:
	1. fast Peeks (praktisch Sattelpunkt) ausfindig machen und sie bei der Muster suche mit beachten
	
	
Informationen
	1. 	Derzeit ist es lediglich möglich WAV-Dateien mit maximal 2147483647.
		Dies entspricht bei einer Frame-Rate von 44100 ca. 13,5 Stunden Laufzeit.
		Diese Einschränkung besteht, da die einzelnen Samples in einem Array abgelegt werden und ein Array maximal 2147483647(= größte int-Zahl) Elemente enthalten kann.
	2.	Derzeit kann das Tool nur den ersten Channel einer WAV-Datei analysieren. Alle weiteren Channels einer WAV-Datei werden ignoriert!