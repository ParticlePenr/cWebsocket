package br.com.battlepassCalculatorValorant.model.Observer

interface IObservable {
    val observers: ArrayList<IObserver>

    fun add(observer: IObserver) {
        observers.add(observer)
    }

    fun remove(observer: IObserver) {
        observers.remove(observer)
    }

    fun deleteAll() {
        observers.clear()
    }

    fun sendUpdateEvent() {
        observers.forEach { it.update() }
    }
}