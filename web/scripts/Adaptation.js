class Adaptation {
    constructor(name, state, styleConfig, strength) {
        this._name = name;
        this._state = state;
        this._styleConfig = styleConfig;
        this._strength = strength;
    }

    set name(value) {
        this._name = value;
    }

    set state(value) {
        this._state = value;
    }

    set styleConfig(value) {
        this._styleConfig = value;
    }

    set strength(value) {
        this._strength = value;
    }

    get name() {
        return this._name;
    }

    get state() {
        return this._state;
    }

    get styleConfig() {
        return this._styleConfig;
    }


    get strength() {
        return this._strength;
    }
}