class Adaptation {
    constructor(name, state, styleConfig) {
        this._name = name;
        this._state = state;
        this._styleConfig = styleConfig;
    }

    set name(value) {
        this._name = value;
    }

    set state(value) {
        this._state = value;
    }

    set settings(value) {
        this._styleConfig = value;
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
}