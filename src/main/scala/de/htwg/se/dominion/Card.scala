package de.htwg.se.dominion

enum Card(val name:String, val cost:Int, val value:Int, val points:Int, val amount:Int):
    def getName: String = name
    def getCost: Int = cost
    def getValue: Int = value
    def getPoints: Int = points
    def getAmount: Int = amount

    case Kupfer extends Card("Kupfer", 0, 1, 0, 50)
    case Silber extends Card("Silber", 3, 2, 0, 50)
    case Gold extends Card("Gold", 6, 3, 0, 50)
    case Anwesen extends Card("Anwesen", 2, 0, 1, 50)
    case Herzogtum extends Card("Herzogtum", 5, 0, 3, 25)
    case Provinz extends Card("Provinz", 8, 0, 6, 12)
    case Fluch extends Card("Fluch", 0, 0, -1, 50)

    case Burggraben extends Card("Burggraben", 2, 0, 0, 10)
    case Kapelle extends Card("Kapelle", 2, 0, 0, 10)
    case Keller extends Card("Keller", 2, 0, 0, 10) 

    case Dorf extends  Card("Dorf", 3, 0, 0, 10)
    case Holzfaeller extends Card("Holzfaeller", 3, 0, 0, 10)
    case Werkstatt extends Card("Werkstatt", 3, 0, 0, 10)

    case Buerokrat extends Card("Buerokrat", 4, 0, 0, 10)
    case Dieb extends Card("Dieb", 4, 0, 0, 10)
    case Festmahl extends Card("Festmahl", 4, 0, 0, 10)
    case Geldverleiher extends Card("Geldverleiher", 4, 0, 0, 10)
    case Miliz extends Card("Miliz", 4, 0, 0, 10)
    case Schmiede extends Card("Schmiede", 4, 0, 0, 10)
    case Spion extends Card("Spion", 4, 0, 0, 10)
    case Thronsaal extends Card("Thronsaal", 4, 0, 0, 10)
    case Umbau extends Card("Umbau", 4, 0, 0, 10)

    case Bibliothek extends Card("Bibliothek", 5, 0, 0, 10)
    case Hexe extends Card("Hexe", 5, 0, 0, 10)
    case Jahrmarkt extends Card("Jahrmarkt", 5, 0, 0, 10)
    case Laboratorium extends Card("Laboratorium", 5, 0, 0, 10)
    case Markt extends Card("Markt", 5, 0, 0, 10)
    case Mine extends Card("Mine", 5, 0, 0, 10)
    case Ratsversammlung extends Card("Ratsversammlung", 5, 0, 0, 10)

    case Abenteurer extends Card("Abenteurer", 6, 0, 0, 10)
    
    case Garten extends Card("Garten", 4, 0, 0, 10)

    case NotACard extends Card("NotACard", 0, 0, 0, 0)